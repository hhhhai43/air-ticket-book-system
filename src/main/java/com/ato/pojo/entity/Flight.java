package com.ato.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * 航班类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Flight {
    /**
     * 航班id
     */
    private Long id;

    /**
     * 航班号
     */
    private String flightNumber;

    /**
     * 航空公司
     */
    private String airline;

    /**
     * 起飞机场
     */
    private String departureAirport;

    /**
     * 落地机场
     */
    private String arrivalAirport;

    /**
     * 航班日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 起飞时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;

    /**
     * 落地时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime arrivalTime;

    /**
     * 起飞城市
     */
    private String departureCity;

    /**
     * 落地城市
     */
    private String arrivalCity;

    /**
     * 总票数
     */
    private Long totalTickets;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    private LocalDateTime updateTime;
}
