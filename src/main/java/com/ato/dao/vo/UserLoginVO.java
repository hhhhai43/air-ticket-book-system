package com.ato.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户登录返回信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginVO {
    /**
     * 用户id
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
