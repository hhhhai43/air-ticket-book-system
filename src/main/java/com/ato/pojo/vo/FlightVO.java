package com.ato.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightVO {
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
     * 机票金额
     */
    private BigDecimal price;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    /**
     * 起飞时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    LocalTime departureTime;

    /**
     * 落地时间
     */
    @JsonFormat(pattern = "HH:mm:ss", timezone = "GMT+8")
    LocalTime arrivalTime;

    /**
     * 起飞城市
     */
    String departureCity;

    /**
     * 落地城市
     */
    String arrivalCity;
}
