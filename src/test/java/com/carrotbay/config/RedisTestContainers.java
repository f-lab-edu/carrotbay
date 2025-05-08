package com.carrotbay.config;

import org.junit.jupiter.api.DisplayName;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@DisplayName("Redis Test Containers")
@ActiveProfiles("test")
@Testcontainers // Testcontainers 라이브러리를 통해 테스트 환경을 관리하는 어노케이션
@Configuration
public class RedisTestContainers {

	private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
	private static final int REDIS_PORT = 6379;

	@Container
	public static GenericContainer<?> redisContainer = new GenericContainer<>(REDIS_IMAGE)
		.withExposedPorts(REDIS_PORT)
		.withReuse(true); // 컨테이너 재사용 활성화

	@DynamicPropertySource
	private static void registerRedisProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.data.redis.host", redisContainer::getHost);
		registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(REDIS_PORT).toString());
	}
}
