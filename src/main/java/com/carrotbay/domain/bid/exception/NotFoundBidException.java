package com.carrotbay.domain.bid.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundBidException extends RuntimeException {
	public NotFoundBidException(String message) {
		super(message);
	}

	public NotFoundBidException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundBidException(Throwable cause) {
		super(cause);
	}
}
