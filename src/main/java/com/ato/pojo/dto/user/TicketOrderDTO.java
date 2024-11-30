package com.ato.pojo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

/**
 * 订票DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketOrderDTO {
    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 航班日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 乘客信息
     */
    private List<PassengerDTO> passengerDTOs;
}