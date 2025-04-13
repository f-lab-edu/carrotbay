package com.carrotbay.domain.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuthorMismatchException extends RuntimeException {
	public AuthorMismatchException(String message) {
		super(message);
	}

	public AuthorMismatchException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorMismatchException(Throwable cause) {
		super(cause);
	}
}
