package com.carrotbay.dummy;

import org.mindrot.jbcrypt.BCrypt;

import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserStatus;

public class DummyObject {

	protected User newUser(String nickname){
		String hashed = BCrypt.hashpw("test1234T@", BCrypt.gensalt());

		return User.builder()
			.id(1L)
			.username(nickname + "@naver.com")
			.password(hashed)
			.nickname(nickname)
			.status(UserStatus.ACTIVE)
			.introduce("자기소개입니다.")
			.imageUrl("http://image.com")
			.build();
	}

	protected User newMockUser(Long id, String nickname){
		String hashed = BCrypt.hashpw("test1234T@", BCrypt.gensalt());

		return User.builder()
			.id(id)
			.username(nickname + "@naver.com")
			.password(hashed)
			.nickname(nickname)
			.status(UserStatus.ACTIVE)
			.introduce("자기소개입니다.")
			.imageUrl("http://image.com")
			.build();
	}
}
