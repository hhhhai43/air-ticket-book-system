package com.ato.service;

import com.ato.pojo.dto.user.TicketOrderDTO;
import com.ato.pojo.result.Result;

import java.math.BigDecimal;

public interface OrderService {

    Result ticketOrder(TicketOrderDTO ticketOrderDTO);

    Result createOrder(Long userId, Long id, BigDecimal price, TicketOrderDTO ticketOrderDTO);

    Result confirmOrder(Long orderId);

    Result releaseSeats(Long orderId);

    Result cancelOrder(Long orderId);
}