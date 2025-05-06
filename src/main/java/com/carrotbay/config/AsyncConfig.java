package com.carrotbay.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync // @Async 어노테이션이 붙은 메서드를 비동기적으로 실행할 수 있도록 Spring이 비동기 실행 환경을 활성화하는 설정
public class AsyncConfig {
}