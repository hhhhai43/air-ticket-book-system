package com.ato.dao.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailPassengerVO {
    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNumber;
}
