package com.carrotbay.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.carrotbay.domain.users.User;
import com.carrotbay.domain.users.UserStatus;

public class DummyObject {

	protected User newUser(String nickname){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("test1234T@");

		return User.builder()
			.id(1L)
			.username(nickname + "@naver.com")
			.password(encPassword)
			.nickname(nickname)
			.state(UserStatus.DEFAULT)
			.introduce("자기소개입니다.")
			.imageUrl("http://image.com")
			.build();
	}

	protected User newMockUser(Long id, String nickname){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("test1234T@");

		return User.builder()
			.id(id)
			.username(nickname + "@naver.com")
			.password(encPassword)
			.nickname(nickname)
			.state(UserStatus.DEFAULT)
			.introduce("자기소개입니다.")
			.imageUrl("http://image.com")
			.build();
	}
}
