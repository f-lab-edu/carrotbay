package com.carrotbay;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = "spring.redis.enabled=false")
class CarrotbayApplicationTests {

	@Test
	void contextLoads() {
	}

}
