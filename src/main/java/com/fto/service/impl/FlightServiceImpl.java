package com.fto.service.impl;

import com.fto.mapper.FlightMapper;
import com.fto.pojo.dto.emp.FlightDTO;
import com.fto.pojo.entity.Flight;
import com.fto.pojo.result.Result;
import com.fto.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

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
    // TODO 判断数据库是否已存在航班
    @Override
    public Result addFlight(FlightDTO flightDTO) {
        // 保存信息至数据库
        Flight flight = new Flight();
        BeanUtils.copyProperties(flightDTO, flight);
        flightMapper.addFlight(flight);
        // 保存票数至redis
        stringRedisTemplate.opsForValue().set("flight:remain_tickets:" + flight.getId(), flight.getTotalTickets().toString());
        return Result.success();
    }
}
