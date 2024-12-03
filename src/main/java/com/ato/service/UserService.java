package com.ato.service;

import com.ato.dao.result.Result;
import com.ato.dao.dto.user.UserInformationDTO;
import com.ato.dao.dto.user.UserLoginDTO;
import com.ato.dao.dto.user.UserPasswordExchangeDTO;
import com.ato.dao.dto.user.UserRegisterDTO;

public interface UserService {
    Result login(UserLoginDTO userLoginDTO);

    /**
     * 添加用户
     * @param userRegisterDTO
     * @return
     */
    Result register(UserRegisterDTO userRegisterDTO);

    /**
     * 修改密码
     * @param userPasswordExchangeDTO
     * @return
     */
    Result passwordExchange(UserPasswordExchangeDTO userPasswordExchangeDTO);

    /**
     * 查询个人信息
     * @param id
     * @return
     */
    Result userInformation(Long id);

    /**
     * 修改个人信息
     * @param userInformationDTO
     * @return
     */
    Result userInformationUpdate(UserInformationDTO userInformationDTO);
}
