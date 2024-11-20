package com.fto.pojo.dto.user;

import lombok.Data;

/**
 * 用户登录接收信息
 */
@Data
public class UserLoginDTO {
    /**
     *用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
