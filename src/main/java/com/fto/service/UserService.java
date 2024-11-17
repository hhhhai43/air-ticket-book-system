package com.fto.service;

import com.fto.pojo.dto.Result;
import com.fto.pojo.dto.UserLoginDTO;

public interface UserService {
    Result login(UserLoginDTO userLoginDTO);
}
