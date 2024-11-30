package com.ato.controller.user;

import com.ato.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 机票控制类
 */
@Slf4j
@RestController
@RequestMapping("/user/ticket")
public class TicketController {
    @Autowired
    private TicketService ticketService;

}