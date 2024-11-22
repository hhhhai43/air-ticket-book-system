package com.ato.controller.employee;

import com.ato.pojo.dto.emp.FlightDTO;
import com.ato.pojo.dto.emp.FlightPageQueryDTO;
import com.ato.pojo.result.Result;
import com.ato.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 航班控制类
 */
@Slf4j
@RestController
@RequestMapping("/emp/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;

    /**
     * 添加航班信息
     * @param flightDTO
     * @return
     */
    @PostMapping("/addFlight")
    public Result addFlight(@RequestBody FlightDTO flightDTO){
        return flightService.addFlight(flightDTO);
    }

    @GetMapping("/queryFlight")
    public Result pageQueryFlight(@RequestBody FlightPageQueryDTO flightPageQueryDTO){
        log.info("乘客条件查询:{}", flightPageQueryDTO);
        return flightService.pageQueryFlight(flightPageQueryDTO);
    }
}
