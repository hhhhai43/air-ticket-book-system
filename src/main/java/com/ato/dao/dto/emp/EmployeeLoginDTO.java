package com.ato.dao.dto.emp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 员工登录接收信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
