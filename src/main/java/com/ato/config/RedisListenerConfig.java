package com.ato.config;


import com.ato.service.RedisKeyExpirationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 *  redis 远程监听配置类
 */
@Configuration
public class RedisListenerConfig {


    @Bean
    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(connectionFactory);
        return listenerContainer;
    }

    @Bean(name = "redisKeyExpirationListenerConfig")
    public KeyExpirationEventMessageListener redisKeyExpirationListener(RedisMessageListenerContainer listenerContainer) {
        return new RedisKeyExpirationListener(listenerContainer);
    }


}