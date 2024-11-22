package com.ato;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	// TODO
	/**
	 * 航班管理：添加航班，修改航班信息，查询航班信息，（删除航班信息？）
	 *    添加：写入数据库并把一定时间范围内的航班信息写入redis缓存
	 *    修改：修改数据库并写入缓存
	 *    查询：查询数据库
	 *    删除：删除redis缓存？
	 * <p>
	 * 机票管理：购买机票，改签，退票
	 * <p>
	 * 订单管理：查询历史订单，删除订单，恢复历史订单数据
	 */

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
