package com.ato.pojo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    Long id;

    /**
     * 航班号
     */
    String flightNumber;

    /**
     * 航空公司
     */
    String airline;

    /**
     * 起飞机场
     */
    String departureAirport;

    /**
     * 落地机场
     */
    String arrivalAirport;

    /**
     * 航班日期
     */
    Date date;

    /**
     * 起飞时间
     */
    @JsonFormat(pattern = "HH:mm")
    LocalTime departureTime;

    /**
     * 落地时间
     */
    @JsonFormat(pattern = "HH:mm")
    LocalTime arrivalTime;

    /**
     * 起飞城市
     */
    String departureCity;

    /**
     * 落地城市
     */
    String arrivalCity;

    /**
     * 总票数
     */
    Long totalTickets;

    /**
     * 创建时间
     */
    LocalDateTime createTime;

    /**
     * 修改时间
     */
    LocalDateTime updateTime;
}
