package com.ato.service.impl;

import com.ato.constant.JwtClaimsConstant;
import com.ato.constant.MessageConstant;
import com.ato.constant.RootConstant;
import com.ato.context.BaseContext;
import com.ato.mapper.UserMapper;
import com.ato.dao.result.Result;
import com.ato.dao.dto.user.UserInformationDTO;
import com.ato.dao.dto.user.UserLoginDTO;
import com.ato.dao.dto.user.UserPasswordExchangeDTO;
import com.ato.dao.dto.user.UserRegisterDTO;
import com.ato.dao.entity.User;
import com.ato.dao.vo.UserInformationVO;
import com.ato.dao.vo.UserLoginVO;
import com.ato.properties.JwtProperties;
import com.ato.service.UserService;
import com.ato.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 用户登录
     * @param userLoginDTO
     * @return
     */
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
        claims.put(JwtClaimsConstant.USER_ID, user.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getUserSecretKey(),
                jwtProperties.getUserTtl(),
                claims);

        UserLoginVO userLoginVO = UserLoginVO.builder()
                .id(user.getId())
                .userName(user.getUsername())
                .token(token)
                .build();

        //3、返回实体对象
        return Result.success(userLoginVO);
    }

    /**
     * 新增用户
     * @param userRegisterDTO
     * @return
     */
    @Override
    public Result register(UserRegisterDTO userRegisterDTO) {
        // 查询是否存在用户名
        String username = userRegisterDTO.getUsername();
        if (userMapper.getByUsername(username)!=null){
            return Result.error(MessageConstant.USER+MessageConstant.ALREADY_EXISTS);
        }
        // 新增用户
        String password = DigestUtils.md5DigestAsHex(userRegisterDTO.getPassword().getBytes());
        User user = new User();
        BeanUtils.copyProperties(userRegisterDTO, user);
        user.setIsRoot(RootConstant.ISUSER);
        user.setPassword(password);
        userMapper.addUser(user);
        return Result.success();
    }

    /**
     * 修改密码
     * @param userPasswordExchangeDTO
     * @return
     */
    @Override
    public Result passwordExchange(UserPasswordExchangeDTO userPasswordExchangeDTO) {
        // 检查请求id与当前操作id是否相同
        Long userId = userPasswordExchangeDTO.getId();
        if(!Objects.equals(BaseContext.getCurrentId(), userId)){
            return Result.error(MessageConstant.REQUEST_ERROR);
        }
        //查询原密码是否正确
        String oldPassword = userPasswordExchangeDTO.getOldPassword();
        User user = userMapper.getByUserId(userId);
        if(!Objects.equals(user.getPassword(), DigestUtils.md5DigestAsHex(oldPassword.getBytes()))){
            return Result.error(MessageConstant.WRONG_OLD_PASSWORD);
        }
        //修改密码
        user.setPassword(DigestUtils.md5DigestAsHex(userPasswordExchangeDTO.getNewPassword().getBytes()));
        userMapper.updateUser(user);
        return Result.success();
    }

    /**
     * 用户个人信息查询
     * @param id
     * @return
     */
    @Override
    public Result userInformation(Long id) {
        // 检查请求id与当前操作id是否相同
        if(!Objects.equals(BaseContext.getCurrentId(), id)){
            return Result.error(MessageConstant.REQUEST_ERROR);
        }
        User user = userMapper.getByUserId(id);
        UserInformationVO userInformationVO = new UserInformationVO();
        BeanUtils.copyProperties(user, userInformationVO);
        return Result.success(userInformationVO);
    }

    /**
     * 修改个人信息
     * @param userInformationDTO
     * @return
     */
    @Override
    public Result userInformationUpdate(UserInformationDTO userInformationDTO) {
        // 检查请求id与当前操作id是否相同
        Long id = userInformationDTO.getId();
        if(!Objects.equals(BaseContext.getCurrentId(), id)){
            return Result.error(MessageConstant.REQUEST_ERROR);
        }
        // 获取用户信息并修改
        User user = userMapper.getByUserId(id);
        BeanUtils.copyProperties(userInformationDTO,user);
        userMapper.updateUser(user);
        return Result.success();
    }
}
