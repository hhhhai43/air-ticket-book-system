package com.fto.pojo.dto.user;

import lombok.Data;

@Data
public class UserInformationDTO {
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
