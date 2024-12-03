package com.ato.dao.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightVO {
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
     * 机票金额
     */
    private BigDecimal price;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime departureTime;

    /**
     * 落地时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime arrivalTime;

    /**
     * 起飞城市
     */
    private String departureCity;

    /**
     * 落地城市
     */
    private String arrivalCity;

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
