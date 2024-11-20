package com.fto.controller.user;

import com.fto.pojo.result.Result;
import com.fto.pojo.dto.user.PassengerDTO;
import com.fto.service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user/passenger")
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    @PostMapping("/addPas")
    public Result addPassenger(@RequestBody PassengerDTO passengerDTO){
        return passengerService.addPassenger(passengerDTO);
    }

    /**
     * 分页查询乘客
     */
    // TODO
    @GetMapping("/getpassenger")
    public Result getPassenger(@PathVariable Long id){
        return null;
    }

    /**
     * 删除乘客
     * @param ids
     * @return
     */
    @DeleteMapping("delPassenger")
    public Result delPassenger(@RequestParam List<Long> ids){

        return null;
    }

    /**
     * 修改乘客信息
     */
    @PutMapping
    public Result updatePassenger(@RequestBody PassengerDTO passengerDTO){
        return null;
    }

}
