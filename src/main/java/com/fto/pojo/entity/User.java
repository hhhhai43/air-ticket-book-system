package com.fto.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 员工类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 用户名
     */
    private String Username;

    /**
     * 密码
     */
    private String password;

    /**
     * 权限
     */
    private Integer isRoot;

    /**
     * 邮箱号
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;

    /**
     * 创建时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
