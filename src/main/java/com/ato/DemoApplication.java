package com.ato;

import com.ato.config.WebMvcConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


@EnableAspectJAutoProxy(exposeProxy = true)
@SpringBootApplication
@Configuration
public class DemoApplication{

	// TODO 完事！
	/**
	  <p>
	 机票管理：购买机票，改签，退票

	 购买机票：	** 创建订单 **
	 			** 支付（订单状态改为进行中）**
	 			** 超时或取消订单（订单状态改为已取消）**
	 			** 航班结束（订单状态由进行中改为已完成）**
	 退票：		** 释放座位和机票，订单状态改为已取消 **
	 改签：		** 释放原座位和机票，创建新订单 **
	  <p>
	  订单管理： 	** 查询历史订单，删除订单，恢复历史订单数据 **
	 */
	void nothing(){};


	/**
	 * 已做
	 * 航班管理：添加航班：新增的同时将余票数存入redis，并在redis中创建对应航班的座位表
	 * 		   修改：修改航班的状态
	 * 		   查询：分页条件查询航班
	 *		   删除：(不做删除）
	 */

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}


}
