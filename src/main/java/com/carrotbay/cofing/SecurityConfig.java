package com.carrotbay.cofing;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration //이 클래스가 스프링의 설정 파일임을 나타내며, @Bean 정의를 통해 애플리케이션 컨텍스트에 빈을 등록할 수 있도록 함.
@RequiredArgsConstructor
@EnableWebSecurity
// Spring Security를 활성화하며, 보안 설정을 구성할 수 있도록 WebSecurityConfigurerAdapter 또는 SecurityFilterChain을 사용할 수 있게 함
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) //Spring Security의 메서드 수준 보안 기능을 활성화하며
public class SecurityConfig {

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		String[] paths = {
			"/api/v1/user/**",
		};

		http
			.csrf((csrfConfig) ->
				csrfConfig.disable()
			)
			.headers((headerConfig) ->
				headerConfig.frameOptions(frameOptionsConfig ->
					frameOptionsConfig.disable()
				)
			)
			.authorizeHttpRequests((authorizeRequests) ->
				authorizeRequests
					.requestMatchers("/", "/css/**", "/images/**", "/js/**", "/profile").permitAll()
					.anyRequest().permitAll()
			)
			.formLogin((formLogin) ->
				formLogin.disable()
			)
			.logout((logoutConfig) ->
				logoutConfig.logoutSuccessUrl("/")
			)

		;
		return http.build();
	}

}


