package com.carrotbay.domain.user;

public enum UserStatus {
	ACTIVE("활성화"),
	STOP("정지"),
	DORMANCY("휴면");

	private final String status;

	UserStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public static UserStatus fromString(String status) {
		for (UserStatus userStatus : UserStatus.values()) {
			if (userStatus.getStatus().equals(status)) {
				return userStatus;
			}
		}
		throw new IllegalArgumentException("UserStatus 변환 : " + status);
	}
}

