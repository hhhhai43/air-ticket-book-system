package com.fto.service.impl;

import com.fto.constant.JwtClaimsConstant;
import com.fto.constant.MessageConstant;
import com.fto.constant.RootConstant;
import com.fto.exception.AccountNotFoundException;
import com.fto.mapper.EmpMapper;
import com.fto.pojo.dto.EmployeeLoginDTO;
import com.fto.pojo.dto.Result;
import com.fto.pojo.entity.Employee;
import com.fto.pojo.vo.EmployeeLoginVO;
import com.fto.properties.JwtProperties;
import com.fto.service.EmpService;
import com.fto.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Result login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = empMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());//对前端传过来的密码进行MD5加密
        log.info("密码: {}", password);
        if (!password.equals(employee.getPassword().toLowerCase())) {
            //密码错误
            return Result.error(MessageConstant.PASSWORD_ERROR);
        }

        if (!Objects.equals(employee.getIsRoot(), RootConstant.ISEMPLOYEE)) {
            log.info("密码: {}", password);
            //权限不足
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .token(token)
                .build();

        //3、返回实体对象
        return Result.success(employeeLoginVO);
    }
}
