package com.ato.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
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
import com.ato.pojo.dto.user.ChangeTicketsDTO;
import com.ato.pojo.dto.user.OrderPageQueryDTO;
import com.ato.pojo.dto.user.PassengerDTO;
import com.ato.pojo.dto.user.TicketOrderDTO;
import com.ato.pojo.entity.Flight;
import com.ato.pojo.entity.Order;
import com.ato.pojo.result.PageResult;
import com.ato.pojo.result.Result;
import com.ato.pojo.vo.*;
import com.ato.service.OrderService;
import com.ato.utils.RedisIdWorker;
import com.ato.utils.SimpleRedisLock;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
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
        LocalDateTime flightDepartureDateTime = flight.getDepartureTime();
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

    @Transactional
    public Result createOrder(Long userId, Long flightId, BigDecimal price, TicketOrderDTO ticketOrderDTO) {
        List<PassengerDTO> passengers = ticketOrderDTO.getPassengerDTOs();
        if (passengers == null || passengers.isEmpty()) {
            return Result.error("乘客信息不能为空");
        }

        // 判断库存是否足够
        String flightKey = ticketOrderDTO.getFlightNumber() + "_" + ticketOrderDTO.getDate().toString();

        if (passengers.size() > Integer.parseInt(
                Objects.requireNonNull(stringRedisTemplate.opsForValue()
                        .get(RedisConstants.REMAIN_TICKETS_KEY + flightKey)))) {
            return Result.error("余票不足");
        }

        // 判断乘客是否已经有人购买该次航班
        Map<Object, Object> seatMap = stringRedisTemplate.opsForHash().entries(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey);

        for (PassengerDTO passenger : passengers) {
            if (seatMap.containsValue(passenger.getIdNumber())) {
                return Result.error("已有乘客购买该次航班");
            }
        }


        Flight flight = flightMapper.queryById(flightId);

        // 乘客未购买机票，机票数量减少，为乘客分配座位
        stringRedisTemplate.opsForValue().increment(RedisConstants.REMAIN_TICKETS_KEY + flightKey, -passengers.size());

        // 分配座位并记录每个乘客的座位号
        int availableSeatsCount = 0;
        Map<String, String> seatAssignments = new HashMap<>();
        List<OrderPassengerVO> orderPassengerVOs = new ArrayList<>();  // 用于存储带座位号的乘客信息

        // 遍历当前航班的座位信息（seatMap），检查每个座位是否已经被占用
        for (Map.Entry<Object, Object> entry : seatMap.entrySet()) {
            // 获取当前座位号（key）和已经占用该座位的乘客 ID（value）
            String seatNumber = (String) entry.getKey();  // 座位号
            String currentPassengerId = (String) entry.getValue();  // 当前占用该座位的乘客 ID

            // 如果该座位当前未被占用（currentPassengerId 为 null 或空字符串），则可以为乘客分配该座位
            if (currentPassengerId == null || currentPassengerId.isEmpty()) {
                // 需要分配的乘客还没有结束
                if (availableSeatsCount < passengers.size()) {
                    // 创建一个 OrderPassengerVO 对象，用于存储乘客信息和座位号
                    OrderPassengerVO orderPassengerVO = new OrderPassengerVO();

                    // 将乘客的基本信息从 passengers 列表中复制到 orderPassengerVO 对象
                    BeanUtils.copyProperties(passengers.get(availableSeatsCount), orderPassengerVO);

                    // 设置乘客的座位号
                    orderPassengerVO.setSeatNumber(seatNumber);  // 将当前未占用的座位号分配给乘客

                    // 将这个带座位号的乘客信息加入到 orderPassengerVOs 列表中
                    orderPassengerVOs.add(orderPassengerVO);

                    // 更新座位的占用情况，将座位号和对应的乘客 ID 存入座位分配表（seatAssignments）
                    seatAssignments.put(seatNumber, passengers.get(availableSeatsCount).getIdNumber());

                    // 增加已分配座位的计数
                    availableSeatsCount++;
                }
            }

            // 如果已经为所有乘客分配了座位，则跳出循环
            if (availableSeatsCount == passengers.size()) {
                break;  // 结束循环，不再继续分配座位
            }
        }


        // 保存座位表
        stringRedisTemplate.opsForHash().putAll(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey, seatAssignments);

        // 创建订单
        Order order = new Order();
        long orderId = redisIdWorker.nextId(RedisConstants.FLIGHT_ORDER_PREFIX);
        order.setId(orderId);
        order.setStatus(OrderStatus.PENDING);
        order.setFlightId(flightId);
        order.setTotalAmount(price.multiply(BigDecimal.valueOf(passengers.size())));
        order.setUserId(userId);

        // 将乘客信息保存到 order_passenger 表中
        List<Map<String, Object>> orderPassengerList = new ArrayList<>();
        for (OrderPassengerVO orderPassengerVO : orderPassengerVOs) {
            Map<String, Object> map = new HashMap<>();
            map.put("orderId", orderId);
            map.put("passengerId", passengerMapper.getIdByIdNumber(orderPassengerVO.getIdNumber()));
            orderPassengerList.add(map);
        }

        if (!orderPassengerList.isEmpty()) {
            orderMapper.saveOrderPassengersBatch(orderPassengerList);
        }

        // 将乘客信息保存到 Redis 中
        try {
            // 使用 Jackson 将带座位号的乘客信息序列化为 JSON
            String passengersJson = jacksonObjectMapper.writeValueAsString(orderPassengerVOs);
            // 保存到 Redis
            stringRedisTemplate.opsForValue().set(RedisConstants.FLIGHT_ORDER_PREFIX + orderId, passengersJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return Result.error("订单处理失败");
        }

        // 设置座位表的过期时间，假设航班结束时间为 arrivalTime
        LocalDateTime arrivalTime = flight.getArrivalTime();
        Duration duration = Duration.between(LocalDateTime.now(), arrivalTime);  // 计算从现在到航班结束的剩余时间
        long expirationTimeInSeconds = duration.getSeconds(); // 转换为秒

        // 设置过期时间
        if (expirationTimeInSeconds > 0) {
            stringRedisTemplate.expire(RedisConstants.FLIGHT_ORDER_PREFIX + orderId, expirationTimeInSeconds, TimeUnit.SECONDS);
        }
        // 设置订单超时时间
        stringRedisTemplate.opsForValue().set(RedisConstants.FLIGHT_ORDER_PREFIX + RedisConstants.COUNTDOWN + orderId, "");
        stringRedisTemplate.expire(RedisConstants.FLIGHT_ORDER_PREFIX + RedisConstants.COUNTDOWN + orderId, 30, TimeUnit.MINUTES);
        orderMapper.saveOrder(order);

        // 返回订单数据
        OrderVO orderVO = new OrderVO();
        FlightVO flightVO = new FlightVO();
        BeanUtils.copyProperties(order, orderVO);
        BeanUtils.copyProperties(flight, flightVO);
        orderVO.setFlightVO(flightVO);
        orderVO.setPassengers(orderPassengerVOs);  // 返回 OrderPassengerVO 列表

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
        List<OrderPassengerVO> passengers;
        passengers = JSON.parseObject(passengersJson, new TypeReference<List<OrderPassengerVO>>(){});

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
        orderVO.setPassengers(passengers);

        // 7. 删除 Redis 中的订单信息
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
        // 修改订单为已取消
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
        List<OrderPassengerVO> passengers;
        String orderKey = RedisConstants.FLIGHT_ORDER_PREFIX + orderId;
        log.info("orderKey:{}",orderKey);
        String passengersJson = stringRedisTemplate.opsForValue().get(orderKey);
        log.info("passengerJson:{}",passengersJson);
        passengers = JSON.parseObject(passengersJson, new TypeReference<List<OrderPassengerVO>>(){});
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
        for (OrderPassengerVO passenger : passengers) {
            String seatNumber = passengerToSeatMap.get(passenger.getIdNumber());
            if (seatNumber != null) {
                // 释放座位
                stringRedisTemplate.opsForHash().put(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey, seatNumber, "");
            }
        }

        // 释放机票数
        stringRedisTemplate.opsForValue().increment(RedisConstants.REMAIN_TICKETS_KEY + flightKey, passengers.size());

        // 删除订单乘客信息
        stringRedisTemplate.delete(orderKey);

        // 6. 订单确认成功，返回订单信息
        OrderVO orderVO = new OrderVO();
        FlightVO flightVO = new FlightVO();
        flight = flightMapper.queryById(order.getFlightId());
        BeanUtils.copyProperties(order, orderVO);
        BeanUtils.copyProperties(flight, flightVO);
        orderVO.setFlightVO(flightVO);
        orderVO.setPassengers(passengers);
        return Result.success(orderVO);
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

    /**
     * 退票
     * @param orderId
     * @return
     */
    @Override
    public Result cancelTickets(Long orderId) {
        // TODO 退钱
        return releaseSeats(orderId);
    }

    /**
     * 改签
     * @param changeTicketsDTO
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result changeTickets(ChangeTicketsDTO changeTicketsDTO) {
        // 获取旧订单ID
        Long oldOrderId = changeTicketsDTO.getOldOrderId();

        // 创建新订单
        Result result = (Result) ticketOrder(changeTicketsDTO.getTicketOrderDTO()).getData();
        OrderVO orderVO = (OrderVO) result.getData();

        if (orderVO == null) {
            // 如果新订单创建失败，返回错误信息
            return Result.error(result.getMsg());
        }

        // 如果新订单创建成功，删除旧订单
        try {
            cancelOrder(oldOrderId);
        } catch (Exception e) {
            // 如果删除旧订单失败，返回错误信息
            return Result.error("改签失败，无法取消旧订单");
        }

        // 返回确认订单的结果
        return confirmOrder(orderVO.getId());
    }

    /**
     * 分页查询订单
     * @param orderPageQueryDTO
     * @return
     */
    @Override
    public Result pageQuery(OrderPageQueryDTO orderPageQueryDTO) {
        //select * from passenger limit 0,10
        PageHelper.startPage(orderPageQueryDTO.getPage(),orderPageQueryDTO.getPageSize());
        Page<OrderPageQueryVO> page= orderMapper.pageQuery(orderPageQueryDTO);
        log.info("page:{}",page);

        long total = page.getTotal();
        List<OrderPageQueryVO> records = page.getResult();
        PageResult pageResult = new PageResult(total, records);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询历史订单
     * @param orderId
     * @return
     */
    @Override
    public Result queryById(Long orderId) {
        // TODO 检查当前用户是否为订单拥有者
        String key = RedisConstants.HISTORYORDER + orderId;
        // 先从redis中查询
        String orderDetailJson = stringRedisTemplate.opsForValue().get(key);
        if(StrUtil.isNotBlank(orderDetailJson)){
            return Result.success(JSONUtil.toBean(orderDetailJson,HistoryOrderVO.class));
        }

        // 缓存中没有，从数据库查询
        HistoryOrderVO histOryorderVO = new HistoryOrderVO();
        // 获取订单基本信息
        Order order = orderMapper.queryById(orderId);

        // 获取航班信息
        Flight flight = flightMapper.queryById(order.getFlightId());
        FlightVO flightVO = new FlightVO();
        BeanUtils.copyProperties(flight, flightVO);

        // 获取乘客信息
        List<Long> ids = orderMapper.getPassengers(orderId);
        List<OrderDetailPassengerVO> passengers = passengerMapper.getByIds(ids);

        BeanUtils.copyProperties(order, histOryorderVO);
        histOryorderVO.setFlightVO(flightVO);
        histOryorderVO.setPassengers(passengers);

        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(histOryorderVO), RedisConstants.CACHE_ORDER_TTL, TimeUnit.MINUTES);
        return Result.success(histOryorderVO);
    }

    /**
     * 删除订单
     * @param orderId
     * @return
     */
    @Override
    public Result deleteOrder(Long orderId) {
        orderMapper.deleteOrderById(orderId);
        orderMapper.deletePassengerInOrder(orderId);

        // 删除 Redis 缓存中的订单数据
        String key = RedisConstants.HISTORYORDER + orderId;
        stringRedisTemplate.delete(key);
        return Result.success();
    }


}
