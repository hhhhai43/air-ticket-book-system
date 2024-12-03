package com.ato.dao.dto.emp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 延误信息DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DelayDTO {
    /**
     * 航班id
     */
    private Long id;

    /**
     * 延误状态
     */
    private boolean delay;

    /**
     * 延误时间
     */
    private Long delayTime;

    /**
     * 延误原因
     */
    private String delayReason;
}
