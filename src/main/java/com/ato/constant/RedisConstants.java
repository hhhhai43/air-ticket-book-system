package com.ato.constant;

public class RedisConstants {
    public static final String REMAIN_TICKETS_KEY = "remainTickets";
    public static final String FLIGHT_SEATS_PREFIX = "flight:seats:";
    public static final String FLIGHT_ORDER_PREFIX = "flight:order:";
    public static final String COUNTDOWN = "expire:";
    public static final String HISTORYORDER = "historyOrder:";
    public static final long CACHE_ORDER_TTL = 30L;
}
