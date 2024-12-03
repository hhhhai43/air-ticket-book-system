package com.ato.dao.vo;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPassengerVO {
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

    /**
     * 座位号
     */
    private String seatNumber;
}
