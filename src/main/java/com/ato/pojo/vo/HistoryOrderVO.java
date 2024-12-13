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
import java.util.List;

/**
 * 订单VO类
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryOrderVO {
    //订单：id、金额、航班信息（日期、航班号、航空公司、起落时间、起落城市、起落机场）、乘客列表、订单状态

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
     * 航班信息
     */
    private FlightVO flightVO;

    /**
     * 乘客列表
     */
    private List<OrderDetailPassengerVO> passengers;
}
