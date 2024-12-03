package com.ato.controller.user;

import com.ato.dao.dto.user.FlightPageQueryDTO;
import com.ato.dao.result.Result;
import com.ato.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户航班控制类
 */
@Slf4j
@RestController("userFlightController")
@RequestMapping("/user/flight")
public class FlightController {
    @Autowired
    private FlightService flightService;

    /**
     * 航班查询
     * @param flightPageQueryDTO
     * @return
     */
    @GetMapping("/queryFlight")
    public Result pageQueryFlight(FlightPageQueryDTO flightPageQueryDTO){
        log.info("航班条件查询:{}", flightPageQueryDTO);
        return flightService.pageQueryFlight(flightPageQueryDTO);
    }
}
