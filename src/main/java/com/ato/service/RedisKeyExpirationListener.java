package com.ato.service;

import com.ato.constant.RedisConstants;
import com.ato.enumeration.OrderStatus;
import com.ato.mapper.FlightMapper;
import com.ato.mapper.OrderMapper;
import com.ato.pojo.entity.Flight;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

/**
 * Redis超时监听器
 */
@Service
@Slf4j
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private FlightMapper flightMapper;

    @Autowired
    private OrderMapper orderMapper;

    public RedisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        super(listenerContainer);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            // 获取过期键的字节数组并转换为字符串
            String expiredKey = new String(message.getBody(), StandardCharsets.UTF_8);

            /**
              订单超时删除
             */
            // 判断接收到的消息是否包含特定的过期键前缀（即 COUNTDOWN 键）
            if (expiredKey.contains(RedisConstants.COUNTDOWN)) {

                // 从过期键中提取出订单 ID（去除前缀）
                // expiredKey 格式通常为：flight_order:1234:countdown
                // 需要去掉 RedisConstants.FLIGHT_ORDER_PREFIX 和 RedisConstants.COUNTDOWN 来提取订单 ID
                String orderIdStr = expiredKey.replace(RedisConstants.FLIGHT_ORDER_PREFIX + RedisConstants.COUNTDOWN, "");

                // 确保 orderIdStr 转换为 Long 类型
                Long orderId = null;
                try {
                    orderId = Long.valueOf(orderIdStr);
                } catch (NumberFormatException e) {
                    // 处理非法的订单 ID 格式
                    log.error("订单 ID 格式错误: " + orderIdStr, e);
                    return;  // 如果格式错误则不继续处理
                }

                // 调用 orderService 的 releaseSeats 方法来释放订单相关的座位和库存
                // 这个方法会根据订单 ID 执行相应的座位释放操作
                orderService.releaseSeats(orderId);
            }

            /**
             * 航班结束后更新订单状态
             */
            // 判断过期的 key 是否是座位信息的 key (例如：FLIGHT_SEATS_PREFIX + flightKey)
            if (expiredKey.startsWith(RedisConstants.FLIGHT_SEATS_PREFIX)) {
                // 提取航班号和日期
                String flightKey = expiredKey.substring(RedisConstants.FLIGHT_SEATS_PREFIX.length());
                String[] parts = flightKey.split("_");
                if (parts.length == 2) {
                    String flightNumber = parts[0];
                    String date = parts[1];

                    // 根据航班号和日期获取航班 ID
                    Flight flight = flightMapper.findByFlightNumberAndDate(flightNumber, date);
                    if (flight != null) {
                        // 更新相关订单状态为“已结束”
                        orderMapper.updateOrderStatusByFlightId(flight.getId(), String.valueOf(OrderStatus.COMPLETED));
                    }
                }
            }


        } catch (Exception e) {
            // 记录异常信息并继续监听其他消息
            log.error("处理 Redis 过期消息时出错", e);
        }
    }
}
