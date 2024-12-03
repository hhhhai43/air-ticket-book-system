package com.ato.dao.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 添加乘客信息类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerDTO {

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 电话号码
     */
    private String phonenumber;
}
