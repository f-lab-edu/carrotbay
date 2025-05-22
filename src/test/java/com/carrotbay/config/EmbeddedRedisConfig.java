package com.carrotbay.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.test.context.TestConfiguration;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import redis.embedded.RedisServer;

@TestConfiguration //  Spring에서 테스트 전용 설정 클래스를 정의할 때 사용하는 어노테이션

public class EmbeddedRedisConfig {
	private static final int REDIS_PORT = 6379;
	private RedisServer redisServer;

	// Bean 초기화 후에 자동으로 호출되는 메서드를 지정하는 어노테이션으로 EmbeddedRedisConfig Bean이 생성된 후 Redis 서버를 시작하기 위해 사용한다.
	@PostConstruct
	public void redisServer() throws IOException {
		int port = isRedisRunning() ? findAvailablePort() : REDIS_PORT;
		redisServer = new RedisServer(port);
		redisServer.start();
	}

	// Bean이 소멸되기 전에 자동으로 호출되는 메서드를 지정하는 어노테이션으로 테스트가 종료되거나 EmbeddedRedisConfig Bean이 파괴될 때 Redis 서버를 중지하기 위해 사용한다.
	@PreDestroy
	public void stopRedis() {
		if (redisServer != null) {
			try {
				redisServer.stop();
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private boolean isRedisRunning() throws IOException {
		return isRunning(executeGrepProcessCommand(REDIS_PORT));
	}

	public int findAvailablePort() throws IOException {

		for (int port = 10000; port <= 65535; port++) {
			Process process = executeGrepProcessCommand(port);
			if (!isRunning(process)) {
				return port;
			}
		}

		throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
	}

	private Process executeGrepProcessCommand(int port) throws IOException {
		String command = String.format("netstat -nat | grep LISTEN|grep %d", port);
		String[] shell = {"/bin/sh", "-c", command};
		return Runtime.getRuntime().exec(shell);
	}

	private boolean isRunning(Process process) {
		String line;
		StringBuilder pidInfo = new StringBuilder();

		try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

			while ((line = input.readLine()) != null) {
				pidInfo.append(line);
			}

		} catch (Exception e) {
		}

		return !StringUtils.isEmpty(pidInfo.toString());
	}
}
