package com.ato.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerVO {
    /**
     * 乘客id
     */
    private Long id;

    /**
     * 乘客姓名
     */
    private String name;

    /**
     * 电话号码
     */
    private String phonenumber;

    /**
     * 身份证号
     */
    private String idNumber;
}
