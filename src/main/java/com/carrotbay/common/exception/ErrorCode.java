package com.carrotbay.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum ErrorCode {

	// 402 BAD_REQUEST 잘못된 요청
	INVALID_PARAMETER(402, "입력 값을 다시 한번 확인해주세요."),
	USER_EXIST(402, "해당 사용자가 이미 존재합니다."),
	VALIDATION_ERROR(402, "유효성 검사 실패: 필드 값을 확인하세요."),
	NICKNAME_EXIST(402, "이미 해당 닉네임이 존재합니다."),

	// 500
	USER_NOT_EXIST(500, "해당 사용자가 존재하지않습니다."),
	SESSION_ACTIVE(500, "이미 로그인한 사용자입니다."),
	;

	private final int status;
	private final String message;

	// 생성자 정의
	ErrorCode(int status, String message) {
		this.status = status;
		this.message = message;
	}
}
