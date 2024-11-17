package com.fto.controller.employee;

import com.fto.pojo.dto.EmployeeLoginDTO;
import com.fto.pojo.dto.Result;
import com.fto.properties.JwtProperties;
import com.fto.service.EmpService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/emp/emp")
public class EmpController {
    @Autowired
    private EmpService empService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    public Result login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        return empService.login(employeeLoginDTO);
    }

}
