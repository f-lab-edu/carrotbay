package com.carrotbay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.redis.enabled=false")
class CarrotbayApplicationTests {

	@Test
	void contextLoads() {
	}

}
