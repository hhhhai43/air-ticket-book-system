package com.ato;

import com.ato.constant.RedisConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootTest
class DemoApplicationTests {
	@Autowired
	private StringRedisTemplate stringRedisTemplate;


	@Test
	void contextLoads() {
	}

	@Test
	void redisTest() throws ParseException {
		// 使用 SimpleDateFormat 格式化日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = sdf.parse("2025-01-13");

		// 格式化日期为符合预期的字符串格式
		String formattedDate = sdf.format(date);

		// 使用格式化后的日期构建 Redis 键
		String redisKey =  "AK1234" + "_" + formattedDate;

		// 打印 Redis 中获取的值
		System.out.println(stringRedisTemplate.opsForHash().get(RedisConstants.REMAIN_TICKETS_KEY , redisKey));
	}

}