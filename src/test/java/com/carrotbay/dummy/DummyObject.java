package com.carrotbay.dummy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserStatus;

public class DummyObject {

	protected User newUser(String nickname){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("test1234T@");

		return User.builder()
			.id(1L)
			.username(nickname + "@naver.com")
			.password(encPassword)
			.nickname(nickname)
			.status(UserStatus.ACTIVE)
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
			.status(UserStatus.ACTIVE)
			.introduce("자기소개입니다.")
			.imageUrl("http://image.com")
			.build();
	}
}
