package com.ato.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import java.util.Arrays;

@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        //corsConfig.addAllowedOrigin("http://localhost:5173"); // 允许所有域访问，生产环境中应根据需要进行配置
        corsConfig.addAllowedOrigin("*"); // 允许所有域访问，生产环境中应根据需要进行配置

        // corsConfig.addAllowedOriginPattern("*"); // 允许所有域访问，但要配合 allowCredentials 使用
        corsConfig.addAllowedMethod("*"); // 允许所有HTTP方法
        corsConfig.addAllowedHeader("*"); // 允许所有HTTP头部
        //corsConfig.setAllowCredentials(true); // 允许发送凭据

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsFilter(source);
    }
}