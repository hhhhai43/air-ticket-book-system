package com.ato.pojo.dto.emp;

import lombok.Data;

/**
 * 员工登录接收信息
 */
@Data
public class EmployeeLoginDTO {
    /**
     *用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;
}
