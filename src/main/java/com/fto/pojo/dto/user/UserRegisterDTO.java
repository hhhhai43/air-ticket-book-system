package com.fto.pojo.dto.user;

import lombok.Data;

/**
 * 用户注册信息
 */
@Data
public class UserRegisterDTO {
    /**
     *用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
