package com.ato.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ato.constant.RedisConstants;
import com.ato.context.BaseContext;
import com.ato.enumeration.OrderStatus;
import com.ato.json.JacksonObjectMapper;
import com.ato.mapper.FlightMapper;
import com.ato.mapper.OrderMapper;
import com.ato.mapper.PassengerMapper;
import com.ato.pojo.dto.emp.FlightDTO;
import com.ato.pojo.dto.user.PassengerDTO;
import com.ato.pojo.dto.user.TicketOrderDTO;
import com.ato.pojo.entity.Flight;
import com.ato.pojo.entity.Order;
import com.ato.pojo.result.Result;
import com.ato.pojo.vo.FlightVO;
import com.ato.pojo.vo.OrderVO;
import com.ato.service.OrderService;
import com.ato.utils.RedisIdWorker;
import com.ato.utils.SimpleRedisLock;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 订单服务类
 */
@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private JacksonObjectMapper jacksonObjectMapper;  // 引入自定义的 JacksonObjectMapper

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private RedisIdWorker redisIdWorker;

    @Autowired
    private PassengerMapper passengerMapper;

    /**
     * 订购机票
     * @param ticketOrderDTO
     * @return
     */
    @Override
    public Result ticketOrder(TicketOrderDTO ticketOrderDTO) {
        // 判断航班时间是否允许购买
        FlightDTO flightDTO = new FlightDTO();
        flightDTO.setFlightNumber(ticketOrderDTO.getFlightNumber());
        flightDTO.setDate(ticketOrderDTO.getDate());
        List<Flight> flights = flightMapper.query(flightDTO);
        if(flights.size()!=1)return Result.error("服务器错误");
        Flight flight = flights.get(0);

        // 合成航班的起飞时间（日期 + 起飞时间）
        LocalDateTime flightDepartureDateTime = LocalDateTime.of(flight.getDate(), flight.getDepartureTime());
        // 判断当前时间是否在航班起飞前两小时内
        if (LocalDateTime.now().isAfter(flightDepartureDateTime.minusHours(2))) {
            return Result.error("当前时间不再销售该机票");
        }

        // 获取用户
        Long userId = BaseContext.getCurrentId();
        SimpleRedisLock lock = new SimpleRedisLock(stringRedisTemplate, "order:userId");
        boolean isLock = lock.tryLock(1200);
        if(!isLock){
            //获取锁失败
            return Result.error("当前有订单正在处理");
        }
        try{
            OrderService proxy = (OrderService) AopContext.currentProxy();
            return proxy.createOrder(userId, flight.getId(),flight.getPrice(),ticketOrderDTO);
        }finally {
            lock.unlock();
        }
    }

    /**
     * 创建订单
     * @param userId
     * @param flightId
     * @param price
     * @param ticketOrderDTO
     * @return
     */
    @Transactional
    public Result createOrder(Long userId, Long flightId, BigDecimal price, TicketOrderDTO ticketOrderDTO) {
        List<PassengerDTO> passengers = ticketOrderDTO.getPassengerDTOs();
        if (passengers == null || passengers.isEmpty()) {
            return Result.error("乘客信息不能为空");
        }
        // 判断库存是否足够
        String flightKey = ticketOrderDTO.getFlightNumber() + "_" + ticketOrderDTO.getDate().toString();

        if(passengers.size() > Integer.parseInt(
                Objects.requireNonNull(stringRedisTemplate.opsForHash()
                        .get(RedisConstants.REMAIN_TICKETS_KEY, flightKey)).toString()))
        {
            return Result.error("余票不足");
        }

        // 判断乘客是否已经有人购买该次航班
        // 获取指定航班号的所有座位信息
        Map<Object, Object> seatMap = stringRedisTemplate.opsForHash().entries(RedisConstants.FLIGHT_SEATS_PREFIX+flightKey);

        // 遍历所有座位信息，查看是否有乘客与目标乘客匹配
        for(PassengerDTO passenger:passengers){
            if(seatMap.containsValue(passenger.getIdNumber())){
                return Result.error("已有乘客购买该次航班");
            }
        }

        // 乘客未购买机票，机票数量减少，为乘客分配座位
        stringRedisTemplate.opsForHash().increment(RedisConstants.REMAIN_TICKETS_KEY, flightKey, -passengers.size());

        // 4. 分配座位（座位号对应乘客ID）
        int passengerCount = passengers.size();
        int availableSeatsCount = 0;

        // 遍历所有座位，寻找空闲座位并进行分配
        Map<String, String> seatAssignments = new HashMap<>();
        for (Map.Entry<Object, Object> entry : seatMap.entrySet()) {
            String seatNumber = (String) entry.getKey();
            String currentPassengerId = (String) entry.getValue();

            // 如果座位当前未被占用（值为null或空），则分配给乘客
            if (currentPassengerId == null || currentPassengerId.isEmpty()) {
                if (availableSeatsCount < passengerCount) {
                    // 为乘客分配该座位
                    seatAssignments.put(seatNumber, passengers.get(availableSeatsCount).getIdNumber());
                    availableSeatsCount++;
                }
            }

            // 如果已经为所有乘客分配了座位，跳出循环
            if (availableSeatsCount == passengerCount) {
                break;
            }
        }

        // 保存座位表
        stringRedisTemplate.opsForHash().putAll(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey, seatAssignments);

        // 创建订单并保存订单到数据库
        Order order = new Order();
        long orderId = redisIdWorker.nextId(RedisConstants.FLIGHT_ORDER_PREFIX);
        order.setId(orderId);//订单id
        order.setStatus(OrderStatus.PENDING);//订单状态
        order.setFlightId(flightId);//航班号
        order.setTotalAmount(price.multiply(BigDecimal.valueOf(passengers.size())));//订单金额
        order.setUserId(userId);//用户id

        // 将乘客信息保存到 order_passenger 表中
        //    1. 直接构造批量插入的 List
        List<Map<String, Object>> orderPassengerList = new ArrayList<>();
        for (PassengerDTO passenger : passengers) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);             // 设置订单 ID
            map.put("passengerId", passengerMapper.getIdByIdNumber(passenger.getIdNumber()));  // 设置乘客 ID
            orderPassengerList.add(map);             // 将每个乘客的 orderId 和 passengerId 加入列表
        }

        //    2. 使用批量插入的方法一次性插入所有乘客信息
        if (!orderPassengerList.isEmpty()) {
            orderMapper.saveOrderPassengersBatch(orderPassengerList);
        }

        try {
            // 使用 JacksonObjectMapper 序列化乘客信息为 JSON 字符串
            String passengersJson = jacksonObjectMapper.writeValueAsString(ticketOrderDTO.getPassengerDTOs());
            // 存储订单乘客信息到 Redis 中，使用 String 类型保存 JSON 格式的乘客信息
            stringRedisTemplate.opsForValue().set(RedisConstants.FLIGHT_ORDER_PREFIX + orderId, passengersJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Result.error("订单处理失败");
        }

        // 设置key的超时时间为 30 分钟
        stringRedisTemplate.opsForValue().set(RedisConstants.FLIGHT_ORDER_PREFIX + RedisConstants.COUNTDOWN + orderId, "");
        stringRedisTemplate.expire(RedisConstants.FLIGHT_ORDER_PREFIX + RedisConstants.COUNTDOWN + orderId, 30, TimeUnit.MINUTES);
        orderMapper.saveOrder(order);

        // 返回订单数据
        OrderVO orderVO = new OrderVO();
        FlightVO flightVO = new FlightVO();
        Flight flight = flightMapper.queryById(flightId);
        BeanUtils.copyProperties(order, orderVO);
        BeanUtils.copyProperties(flight,flightVO);
        orderVO.setFlightVO(flightVO);
        orderVO.setPassengerDTOs(ticketOrderDTO.getPassengerDTOs());

        return Result.success(orderVO);
    }

    /**
     * 支付后确认订单
     * @param orderId
     * @return
     */
    @Override
    public Result confirmOrder(Long orderId) {
        Order order = orderMapper.queryById(orderId);
        if(order.getStatus()==OrderStatus.CONFIRMED){
            return Result.error("订单已支付，请勿重复支付");
        }
        // 1. 查询 Redis 中的会过期的 key，检查订单是否超时
        String countdownKey = RedisConstants.FLIGHT_ORDER_PREFIX + RedisConstants.COUNTDOWN + orderId;

        // 2. 如果会过期的 key 不存在或已过期，说明订单超时
        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(countdownKey))) {
            // 释放已占座位
            releaseSeats(orderId);

            // 修改数据库中的订单状态为取消
            orderMapper.updateOrderStatus(orderId, OrderStatus.CANCELLED);

            // 返回错误结果，提示订单已过期
            return Result.error("订单已过期");
        }

        // 3. 获取订单的乘客信息
        String orderKey = RedisConstants.FLIGHT_ORDER_PREFIX + orderId;
        String passengersJson = stringRedisTemplate.opsForValue().get(orderKey);

        // 4. 获取订单的乘客信息
        List<PassengerDTO> passengers;
        passengers = JSON.parseObject(passengersJson, new TypeReference<List<PassengerDTO>>(){});

        // 5. 修改数据库中的订单状态为已确认
        int rowsUpdated = orderMapper.updateOrderStatus(orderId, OrderStatus.CONFIRMED);
        order.setStatus(OrderStatus.CONFIRMED);
        if (rowsUpdated == 0) {
            return Result.error("订单确认失败");
        }

        // 6. 订单确认成功，返回订单信息
        OrderVO orderVO = new OrderVO();
        FlightVO flightVO = new FlightVO();
        Flight flight = flightMapper.queryById(order.getFlightId());
        BeanUtils.copyProperties(order, orderVO);
        BeanUtils.copyProperties(flight, flightVO);
        orderVO.setFlightVO(flightVO);
        orderVO.setPassengerDTOs(passengers);

        // 7. 删除 Redis 中的订单信息
        stringRedisTemplate.delete(orderKey);
        stringRedisTemplate.delete(countdownKey);

        return Result.success(orderVO);
    }

    /**
     * 释放已占座位
     *
     * @param orderId 订单 ID
     * @return
     */
    public Result releaseSeats(Long orderId) {
        log.info("释放座位中:{}",orderId);
        orderMapper.updateOrderStatus(orderId, OrderStatus.CANCELLED);
        // 获取订单信息
        Order order = orderMapper.queryById(orderId);
        if (order == null){
            return Result.error("请求错误");
        }
        Long flightId = order.getFlightId();
        Flight flight = flightMapper.queryById(flightId);
        String flightKey = flight.getFlightNumber() + "_" + flight.getDate().toString();

        // 获取当前航班的座位信息
        Map<Object, Object> seatMap = stringRedisTemplate.opsForHash().entries(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey);
        log.info("座位信息:{}",seatMap);

        // 获取该订单的乘客信息
        List<PassengerDTO> passengers;
        String orderKey = RedisConstants.FLIGHT_ORDER_PREFIX + orderId;
        log.info("orderKey:{}",orderKey);
        String passengersJson = stringRedisTemplate.opsForValue().get(orderKey);
        log.info("passengerJson:{}",passengersJson);
        passengers = JSON.parseObject(passengersJson, new TypeReference<List<PassengerDTO>>(){});
        if(passengers == null){
            return Result.error("请求错误");
        }

        log.info("释放座位的乘客信息:{}",passengers);

        // 构建乘客ID到座位号的映射
        Map<String, String> passengerToSeatMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : seatMap.entrySet()) {
            String seatNumber = (String) entry.getKey();
            String currentPassengerId = (String) entry.getValue();
            if (currentPassengerId != null && !currentPassengerId.isEmpty()) {
                passengerToSeatMap.put(currentPassengerId, seatNumber);
            }
        }

        // 遍历乘客，释放已分配的座位
        for (PassengerDTO passenger : passengers) {
            String seatNumber = passengerToSeatMap.get(passenger.getIdNumber());
            if (seatNumber != null) {
                // 释放座位
                stringRedisTemplate.opsForHash().put(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey, seatNumber, "");
            }
        }

        // 释放机票数
        stringRedisTemplate.opsForHash().increment(RedisConstants.REMAIN_TICKETS_KEY, flightKey, passengers.size());

        // 删除订单乘客信息
        stringRedisTemplate.delete(orderKey);
        return Result.success();
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @Override
    public Result cancelOrder(Long orderId) {
        return releaseSeats(orderId);
    }

}
