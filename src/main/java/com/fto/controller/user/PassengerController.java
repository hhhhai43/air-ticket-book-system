package com.fto.controller.user;

import com.fto.pojo.dto.user.PassengerPageQueryDTO;
import com.fto.pojo.result.PageResult;
import com.fto.pojo.result.Result;
import com.fto.pojo.dto.user.PassengerDTO;
import com.fto.service.PassengerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 乘客控制类add
 */
@Slf4j
@RestController
@RequestMapping("/user/passenger")
public class PassengerController {
    @Autowired
    private PassengerService passengerService;

    /**
     * 添加乘客
     * @param passengerDTO
     * @return
     */
    @PostMapping("/addPas")
    public Result addPassenger(@RequestBody PassengerDTO passengerDTO){
        return passengerService.addPassenger(passengerDTO);
    }

    /**
     * 分页查询乘客
     */
    @GetMapping("/getPassenger")
    public Result getPassenger(PassengerPageQueryDTO passengerPageQueryDTO){
        log.info("员工分页查询，参数为:{}", passengerPageQueryDTO);
        return passengerService.pageQuery(passengerPageQueryDTO);
    }

    /**
     * 删除乘客
     * @param ids
     * @return
     */
    @DeleteMapping("/delPassenger")
    public Result delPassenger(@RequestParam List<Long> ids){
        return passengerService.deleteByIds(ids);
    }

    /**
     * 修改乘客信息
     */
    //TODO
    @PutMapping("/updatePassenger")
    public Result updatePassenger(@RequestBody PassengerDTO passengerDTO){
        return passengerService.updatePassenger(passengerDTO);
    }

}
