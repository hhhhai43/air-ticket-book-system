package com.ato.controller.user;

import com.ato.pojo.dto.user.ChangeTicketsDTO;
import com.ato.pojo.dto.user.TicketOrderDTO;
import com.ato.pojo.result.Result;
import com.ato.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 订单控制类
 */
@Slf4j
@RestController
@RequestMapping("/user/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 下单创建订单
     * @param ticketOrderDTO
     * @return
     */
    @PostMapping("/order")
    public Result ticketOrder(@RequestBody TicketOrderDTO ticketOrderDTO){
        log.info("机票订购信息:{}",ticketOrderDTO);
        return orderService.ticketOrder(ticketOrderDTO);
    }

    /**
     * 支付确认订单
     * @param orderId
     * @return
     */
    @PostMapping("/pay")
    public Result pay(@RequestBody Long orderId){
        log.info("支付订单:{}",orderId);
        // 还要调用支付接口
        return orderService.confirmOrder(orderId);
    }

    /**
     * 取消订单
     * @param orderId
     * @return
     */
    @PostMapping("/cancel")
    public Result cancelOrder(@RequestBody Long orderId){
        log.info("取消订单:{}",orderId);
        return orderService.cancelOrder(orderId);
    }

    /**
     * 退票
     */
    @PostMapping("/cancelTickets")
    public Result cancelTickets(@RequestBody Long orderId){
        log.info("退票:{}",orderId);
        return orderService.cancelTickets(orderId);
    }

    @PostMapping("/changeTickets")
    public Result changeTickets(@RequestBody ChangeTicketsDTO changeTicketsDTO){
        log.info("改签信息:{}",changeTicketsDTO);
        return orderService.changeTickets(changeTicketsDTO);
    }
}
