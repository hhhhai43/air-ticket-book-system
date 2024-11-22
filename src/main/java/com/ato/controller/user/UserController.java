package com.ato.controller.user;

import com.ato.pojo.result.Result;
import com.ato.pojo.dto.user.UserInformationDTO;
import com.ato.pojo.dto.user.UserLoginDTO;
import com.ato.pojo.dto.user.UserPasswordExchangeDTO;
import com.ato.pojo.dto.user.UserRegisterDTO;
import com.ato.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 用户
 */
@Slf4j
@RestController
@RequestMapping("/user/user")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO userLoginDTO) {
        log.info("用户登录：{}", userLoginDTO);
        return userService.login(userLoginDTO);
    }

    /**
     * 用户注册
     * @param userRegisterDTO
     * @return
     */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO userRegisterDTO) {
        log.info("用户注册：{}", userRegisterDTO);
        return userService.register(userRegisterDTO);
    }

    /**
     * 用户修改密码
     * @param userPasswordExchangeDTO
     * @return
     */
    @PutMapping("/exPsw")
    public Result passwordExchange(@RequestBody UserPasswordExchangeDTO userPasswordExchangeDTO){
        log.info("用户修改密码：{}", userPasswordExchangeDTO);
        return userService.passwordExchange(userPasswordExchangeDTO);
    }

    /**
     * 修改个人信息
     * @param userInformationDTO
     * @return
     */
    @PutMapping("/exUserInfo")
    public Result userInformationUpdate(@RequestBody UserInformationDTO userInformationDTO){
        return userService.userInformationUpdate(userInformationDTO);
    }

    /**
     * 查询个人信息
     * @param id
     * @return
     */
    @GetMapping("/info/{id}")
    public Result userInformation(@PathVariable Long id) {
        return userService.userInformation(id);
    }
}
