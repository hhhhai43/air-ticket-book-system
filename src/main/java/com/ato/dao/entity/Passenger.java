package com.ato.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 乘客类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Passenger {
    /**
     * 乘客id
     */
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 身份证号
     */
    private String idNumber;

}
