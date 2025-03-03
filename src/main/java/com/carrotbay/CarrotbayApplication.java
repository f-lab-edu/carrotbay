package com.carrotbay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing //Spring Data JPA에서 엔티티의 자동 생성일자, 수정일자 등을 추적하고 관리하는 JPA Auditing 기능을 활성화하는 데 사용된다.
//어노테이션은 Spring Boot 애플리케이션의 진입점을 정의하는 데 사용된다. 이 어노테이션은 기본적으로 세 가지 주요 어노테이션을 결합한 것으로,
// @Configuration, @EnableAutoConfiguration, @ComponentScan이 포함됨.
@SpringBootApplication
public class CarrotbayApplication {

	public static void main(String[] args) {
		SpringApplication.run(CarrotbayApplication.class, args);
	}

}
