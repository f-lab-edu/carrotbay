package com.carrotbay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@Configuration // Spring 컨테이너가 빈을 구성하고 생성할 수 있는 자바 설정 클래스 역할을 한다.
@RequiredArgsConstructor
public class QueryDslConfig {
	private final EntityManager entityManager;

	@Bean // 메서드 수준에서 사용되며, 해당 메서드가 반환하는 객체를 Spring 컨테이너의 빈으로 등록하도록 지시
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}