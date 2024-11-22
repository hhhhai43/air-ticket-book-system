package com.fto.controller.employee;

import com.fto.pojo.dto.emp.FlightDTO;
import com.fto.pojo.result.Result;
import com.fto.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
