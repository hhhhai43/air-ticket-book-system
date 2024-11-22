package com.ato.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeLoginVO {
    /**
     * 员工id
     */
    private Long id;

    /**
     * 用户名
     */
    private String userName;

    /**
     * JWT令牌
     */
    private String token;
}
