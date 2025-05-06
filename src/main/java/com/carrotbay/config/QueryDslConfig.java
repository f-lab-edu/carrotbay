package com.carrotbay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Configuration // 해당 클래스가 하나 이상의 @Bean을 정의하고 있으며, Spring IoC 컨테이너에서 빈 설정을 위한 클래스임을 나타낸다.
public class QueryDslConfig {

	@PersistenceContext // JPA에서 EntityManager를 주입받을 때 사용되며, 자동으로 영속성 컨텍스트를 관리하도록 한다.
	private EntityManager entityManager;

	@Bean // 개발자가 직접 관리할 객체를 생성하고 Spring 컨테이너에 등록할 때 사용되며, @Configuration 클래스 내부의 메서드에 적용된다.
	public JPAQueryFactory jpaQueryFactory() {
		return new JPAQueryFactory(entityManager);
	}
}
