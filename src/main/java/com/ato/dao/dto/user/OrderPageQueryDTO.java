package com.ato.dao.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 乘客分页查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageQueryDTO {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 页码
     */
    private int page;

    /**
     *  每页显示记录数
     */
    private int pageSize;
}
