package com.ato.enumeration;

/**
 * 订单状态枚举
 */
public enum OrderStatus {
    /*
     * 等待支付 ->取消订单
     *         ->已支付
     * 已支付   ->退票 取消订单
     *         ->已完成
     * 已取消
     * 已完成
     */


    /**
     * 等待支付
     */
    PENDING,

    /**
     * 已支付,
     */
    CONFIRMED,

    /**
     * 已取消
     */
    CANCELLED,

    /**
     * 已完成
     */
    COMPLETED
}