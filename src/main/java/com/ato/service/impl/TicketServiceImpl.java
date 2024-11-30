package com.ato.service.impl;

import com.ato.mapper.TicketMapper;
import com.ato.service.TicketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 机票服务类
 */
@Slf4j
@Service
public class TicketServiceImpl implements TicketService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TicketMapper ticketMapper;


}
