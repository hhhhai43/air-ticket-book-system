package com.ato.service.impl;

import com.ato.constant.MessageConstant;
import com.ato.constant.RedisConstants;
import com.ato.mapper.FlightMapper;
import com.ato.dao.dto.emp.DelayDTO;
import com.ato.dao.dto.emp.FlightDTO;
import com.ato.dao.dto.user.FlightPageQueryDTO;
import com.ato.dao.entity.Flight;
import com.ato.dao.result.PageResult;
import com.ato.dao.result.Result;
import com.ato.dao.vo.FlightVO;
import com.ato.service.FlightService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class FlightServiceImpl implements FlightService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private FlightMapper flightMapper;

    /**
     * 添加航班信息
     * @param flightDTO
     * @return
     */
    @Override
    public Result addFlight(FlightDTO flightDTO) {
        String flightKey = flightDTO.getFlightNumber() + "_" + flightDTO.getDate();//票数的field

        // 保存信息至数据库
        List<Flight> flights = flightMapper.query(flightDTO);
        if(flights.isEmpty()){
            Flight flight = new Flight();
            BeanUtils.copyProperties(flightDTO, flight);
            flightMapper.addFlight(flight);
            // 将航班票数存储到 Redis 中，票数使用 String 类型存储
            stringRedisTemplate.opsForValue().set(RedisConstants.REMAIN_TICKETS_KEY + flightKey, flight.getTotalTickets().toString());


            //TODO 专门实现一个初始化座位方法，实现多种座位表的添加

            // 初始化座位表
            HashOperations<String, String, String> hashOps = stringRedisTemplate.opsForHash();
            Map<String, String> seatMap = new HashMap<>();

            // 假设航班有40排座位，每排8个座位，座位号从 1A 到 40H
            for (int row = 1; row <= 40; row++) {
                for (char seat = 'A'; seat <= 'H'; seat++) {
                    String seatNumber = row + String.valueOf(seat);
                    seatMap.put(seatNumber, ""); // 空字符串表示座位尚未被占用
                }
            }

            // 将座位信息存储到 Redis 中，使用航班号+日期作为键，座位号作为字段，初始化值为空
            hashOps.putAll(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey, seatMap);

            // 设置座位表的过期时间，假设航班结束时间为 arrivalTime
            LocalDateTime arrivalTime = flightDTO.getArrivalTime();
            Duration duration = Duration.between(LocalDateTime.now(), arrivalTime);  // 计算从现在到航班结束的剩余时间
            long expirationTimeInSeconds = duration.getSeconds(); // 转换为秒

            // 设置过期时间
            if (expirationTimeInSeconds > 0) {
                stringRedisTemplate.expire(RedisConstants.FLIGHT_SEATS_PREFIX + flightKey, expirationTimeInSeconds, TimeUnit.SECONDS);
                stringRedisTemplate.expire(RedisConstants.REMAIN_TICKETS_KEY + flightKey, expirationTimeInSeconds, TimeUnit.SECONDS);
            }

        }else{
            log.info("{}",flights);
            return Result.error(MessageConstant.FLIGHT_ALREADYEXIST);
        }
        return Result.success();
    }

    /**
     * 航班分页查询
     * @param flightPageQueryDTO
     * @return
     */
    @Override
    public Result pageQueryFlight(FlightPageQueryDTO flightPageQueryDTO) {
        flightPageQueryDTO.setCurrentDate(LocalDate.now());
        //select * from passenger limit 0,10
        PageHelper.startPage(flightPageQueryDTO.getPage(),flightPageQueryDTO.getPageSize());
        Page<FlightVO> page= flightMapper.pageQuery(flightPageQueryDTO);
        log.info("{}",page);

        long total = page.getTotal();
        List<FlightVO> records = page.getResult();
        PageResult pageResult = new PageResult(total, records);
        log.info("{}",pageResult);
        return Result.success(pageResult);
    }

    /**
     * 修改航班延迟状态
     * @param delayDTO
     * @return
     */
    @Override
    public Result updateDelay(DelayDTO delayDTO) {
        flightMapper.updateDelay(delayDTO);
        return Result.success();
    }

    /**
     * 查询所有航班
     * @return
     */
    @Override
    public Result getAllFlights() {
        return Result.success(flightMapper.getAllFlights());
    }
}
