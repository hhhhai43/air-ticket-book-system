package com.ato.service.impl;

import com.ato.constant.MessageConstant;
import com.ato.mapper.FlightMapper;
import com.ato.pojo.dto.emp.FlightDTO;
import com.ato.pojo.dto.user.FlightPageQueryDTO;
import com.ato.pojo.entity.Flight;
import com.ato.pojo.result.PageResult;
import com.ato.pojo.result.Result;
import com.ato.pojo.vo.FlightVO;
import com.ato.service.FlightService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

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
        List<Flight> flights = flightMapper.query(flightDTO);
        if(flights.isEmpty()){
            Flight flight = new Flight();
            BeanUtils.copyProperties(flightDTO, flight);
            flightMapper.addFlight(flight);
            // 保存票数至redis
            stringRedisTemplate.opsForValue().set("flight:remain_tickets:" + flight.getId(), flight.getTotalTickets().toString());
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
}
