package com.carrotbay.domain.user;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum UserStatus {
	ACTIVE("활성화"),
	STOP("정지"),
	DORMANCY("휴면");

	private final String status;

	UserStatus(String status) {
		this.status = status;
	}

	@JsonValue //  JSON 데이터를 객체로 변환할 때, 어떤 생성자를 사용해야 할지 지정하는 어노테이션
	public String getStatus() {
		return this.status;
	}

	@JsonCreator // 객체를 JSON으로 직렬화할 때, 어떤 메서드 또는 필드의 값을 JSON 값으로 사용할지 지정하는 어노테이션
	public static UserStatus fromString(String status) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getStatus().equals(status)) {
				return userStatus;
			}
		}
		throw new IllegalArgumentException("UserStatus 변환 : " + status);
	}
}

