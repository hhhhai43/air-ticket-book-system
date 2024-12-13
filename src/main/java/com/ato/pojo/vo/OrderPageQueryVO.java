package com.ato.pojo.vo;

import com.ato.enumeration.OrderStatus;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderPageQueryVO {
    /**
     * 订单id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 订单金额
     */
    private BigDecimal totalAmount;

    /**
     * 订单状态
     */
    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // 订单状态

    /**
     * 起飞时间
     */
    private LocalDateTime departureTime;

    /**
     * 起飞城市
     */
    private String departureCity;

    /**
     * 落地城市
     */
    private String arrivalCity;

    /**
     * 起飞机场
     */
    private String departureAirport;

    /**
     * 落地机场
     */
    private String arrivalAirport;

    /**
     * 航班号
     */
    private String flightNumber;

}
