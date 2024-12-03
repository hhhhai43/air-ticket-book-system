package com.ato.controller.employee;

import com.ato.dao.dto.emp.DelayDTO;
import com.ato.dao.dto.emp.FlightDTO;
import com.ato.dao.result.Result;
import com.ato.service.FlightService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 航班控制类
 */
@Slf4j
@RestController("empFlightController")
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

    /**
     * 修改航班延误状态
     */
    @PutMapping("/updateDelay")
    public Result updateDelay(@RequestBody DelayDTO delayDTO){
        log.info("延误信息：{}",delayDTO);
        return flightService.updateDelay(delayDTO);
    }

    @GetMapping("/getAllFlights")
    public Result getAllFlights(){
        return flightService.getAllFlights();
    }
}
