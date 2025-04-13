package com.carrotbay.domain.review.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class AuctionNotClosedException extends RuntimeException {
	public AuctionNotClosedException(String message) {
		super(message);
	}

	public AuctionNotClosedException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuctionNotClosedException(Throwable cause) {
		super(cause);
	}
}
