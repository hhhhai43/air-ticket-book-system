package com.ato.dao.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangeTicketsDTO {
    /**
     * 改签前机票
     */
    private Long oldOrderId;

    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 航班日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
