package com.fto.service.impl;

import com.fto.constant.JwtClaimsConstant;
import com.fto.constant.MessageConstant;
import com.fto.constant.RootConstant;
import com.fto.mapper.UserMapper;
import com.fto.pojo.dto.Result;
import com.fto.pojo.dto.UserLoginDTO;
import com.fto.pojo.entity.User;
import com.fto.pojo.vo.UserLoginVO;
import com.fto.properties.JwtProperties;
import com.fto.service.UserService;
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
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public Result login(UserLoginDTO userLoginDTO) {
        String username = userLoginDTO.getUsername();
        String password = userLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        User user = userMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (user == null) {
            //账号不存在
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        password = DigestUtils.md5DigestAsHex(password.getBytes());//对前端传过来的密码进行MD5加密
        log.info("密码: {}", password);
        if (!password.equals(user.getPassword().toLowerCase())) {
            //密码错误
            return Result.error(MessageConstant.PASSWORD_ERROR);
        }

        if (!Objects.equals(user.getIsRoot(), RootConstant.ISUSER)) {
            log.info("密码: {}", password);
            //权限不足
            return Result.error(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .token(token)
                .build();

        //3、返回实体对象
        return Result.success(userLoginVO);
    }
}
