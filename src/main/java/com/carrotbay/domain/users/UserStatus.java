package com.carrotbay.domain.users;

public enum UserStatus {

	DEFAULT("기본"),
	STOP("정지"),  // 정지 상태의 코드 값과 메시지
	DORMANCY("휴면"); // 휴면 상태의 코드 값과 메시지

	private final String message;

	// 생성자: 코드와 메시지를 초기화
	UserStatus(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
