package com.carrotbay.domain.user.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundUserException extends RuntimeException {
	public NotFoundUserException(String message) {
		super(message);
	}

	public NotFoundUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundUserException(Throwable cause) {
		super(cause);
	}
}
