package com.ato.service;

import com.ato.dao.dto.user.ChangeTicketsDTO;
import com.ato.dao.dto.user.OrderPageQueryDTO;
import com.ato.dao.dto.user.TicketOrderDTO;
import com.ato.dao.result.Result;

import java.math.BigDecimal;

public interface OrderService {

    Result ticketOrder(TicketOrderDTO ticketOrderDTO);

    Result createOrder(Long userId, Long id, BigDecimal price, TicketOrderDTO ticketOrderDTO);

    Result confirmOrder(Long orderId);

    Result releaseSeats(Long orderId);

    Result cancelOrder(Long orderId);

    Result cancelTickets(Long orderId);

    Result changeTickets(ChangeTicketsDTO changeTicketsDTO);

    Result pageQuery(OrderPageQueryDTO orderPageQueryDTO);

    Result queryById(Long orderId);

    Result deleteOrder(Long orderId);
}