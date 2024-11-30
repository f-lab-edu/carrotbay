package com.carrotbay.common.exception;

import lombok.Getter;

@Getter
public class CustomApiException extends RuntimeException {

	private ErrorCode errorCode;

	public CustomApiException(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}
}