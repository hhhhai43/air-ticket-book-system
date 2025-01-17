package com.ato.pojo.entity;

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
public class Employee {
    //员工id
    private Long id;

    //用户名
    private String Username;

    //密码
    private String password;

    /**
     * 电话号码
     */
    private String phonenumber;

    //权限
    private Integer isRoot;

    //创建时间
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    //修改时间
    //@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

}
