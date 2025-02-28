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

	@JsonValue
	public String getStatus() {
		return this.status;
	}

	@JsonCreator
	public static UserStatus fromString(String status) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getStatus().equals(status)) {
				return userStatus;
			}
		}
		throw new IllegalArgumentException("UserStatus 변환 : " + status);
	}
}

