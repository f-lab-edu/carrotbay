package com.carrotbay.common.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class CustomValidationException extends RuntimeException {
	private ErrorCode errorCode;
	private Map<String, String> errorMap;

	public CustomValidationException(ErrorCode errorCode, Map<String, String> errorMap) {
		this.errorCode = errorCode;
		this.errorMap = errorMap;
	}
}
