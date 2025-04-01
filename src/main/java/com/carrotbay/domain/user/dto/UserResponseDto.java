package com.carrotbay.domain.user.dto;

import com.carrotbay.domain.user.User;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserResponseDto {

	@NoArgsConstructor
	@Getter
	@Setter
	public static class RegisterDto {
		private Long id;
		private String username;
		private String nickname;

		public RegisterDto(User user) {
			this.id = user.getId();
			this.nickname = user.getNickname();
			this.username = user.getUsername();
		}
	}

}
