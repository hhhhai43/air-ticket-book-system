package com.ato.dao.dto.user;

import lombok.Data;

/**
 * 用户修改密码信息接收类
 */
@Data
public class UserPasswordExchangeDTO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 旧密码
     */
    private String oldPassword;

    /**
     * 新密码
     */
    private String newPassword;
}
