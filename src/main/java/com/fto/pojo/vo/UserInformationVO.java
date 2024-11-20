package com.fto.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户个人信息返回类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInformationVO {
    /**
     *用户id
     */
    private Long id;

    /**
     * 邮箱号
     */
    private String email;

    /**
     * 性别
     */
    private Integer gender;
}