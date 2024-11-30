package com.ato.pojo.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 * 航班分页查询DTO
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightPageQueryDTO {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 页码
     */
    private int page;

    /**
     * 每页记录数
     */
    private int pageSize;

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
     * 当前日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate currentDate;

    /**
     * 起飞时间
     */
    @DateTimeFormat(pattern = "HH:mm:ss")
    private LocalTime departureTime;

    /**
     * 起飞城市
     */
    private String departureCity;

    /**
     * 落地城市
     */
    private String arrivalCity;
}
