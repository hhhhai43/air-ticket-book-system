package com.ato.controller.user;

import com.ato.context.BaseContext;
import com.ato.dao.dto.user.ChangeTicketsDTO;
import com.ato.dao.dto.user.OrderPageQueryDTO;
import com.ato.dao.dto.user.TicketOrderDTO;
import com.ato.dao.result.Result;
import com.ato.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 改签
     * @param changeTicketsDTO
     * @return
     */
    @PostMapping("/changeTickets")
    public Result changeTickets(@RequestBody ChangeTicketsDTO changeTicketsDTO){
        log.info("改签信息:{}",changeTicketsDTO);
        return orderService.changeTickets(changeTicketsDTO);
    }

    /**
     * 查询历史订单列表
     * @param orderPageQueryDTO
     * @return
     */
    @GetMapping("/queryOrder")
    public Result pageQueryOrder(OrderPageQueryDTO orderPageQueryDTO){
        orderPageQueryDTO.setUserId(BaseContext.getCurrentId());
        log.info("员工分页查询，参数为:{}", orderPageQueryDTO);
        return orderService.pageQuery(orderPageQueryDTO);
    }

    /**
     * 查询历史订单详情
     * @param orderId
     * @return
     */
    @GetMapping("/orderDetail")
    public Result orderDetail(Long orderId){
        log.info("查询订单:{}详情",orderId);
        return orderService.queryById(orderId);
    }

    @DeleteMapping("/deleteOrder")
    public Result deleteOrder(@RequestParam Long orderId){
        log.info("删除订单:{}",orderId);
        return orderService.deleteOrder(orderId);
    }
}
