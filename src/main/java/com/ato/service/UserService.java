package com.ato.service;

import com.ato.pojo.result.Result;
import com.ato.pojo.dto.user.UserInformationDTO;
import com.ato.pojo.dto.user.UserLoginDTO;
import com.ato.pojo.dto.user.UserPasswordExchangeDTO;
import com.ato.pojo.dto.user.UserRegisterDTO;

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
