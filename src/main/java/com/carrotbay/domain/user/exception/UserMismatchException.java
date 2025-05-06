package com.carrotbay.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserMismatchException extends RuntimeException {
	public UserMismatchException(String message) {
		super(message);
	}

	public UserMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public UserMismatchException(Throwable cause) {
		super(cause);
	}
}
