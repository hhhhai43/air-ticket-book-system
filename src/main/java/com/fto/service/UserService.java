package com.fto.service;

import com.fto.pojo.result.Result;
import com.fto.pojo.dto.user.UserInformationDTO;
import com.fto.pojo.dto.user.UserLoginDTO;
import com.fto.pojo.dto.user.UserPasswordExchangeDTO;
import com.fto.pojo.dto.user.UserRegisterDTO;

public interface UserService {
    Result login(UserLoginDTO userLoginDTO);

    /**
     * 添加用户
     * @param userRegisterDTO
     * @return
     */
    Result register(UserRegisterDTO userRegisterDTO);

    Result passwordExchange(UserPasswordExchangeDTO userPasswordExchangeDTO);

    Result userInformation(Long id);

    Result userInformationUpdate(UserInformationDTO userInformationDTO);
}
