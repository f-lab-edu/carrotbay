package com.carrotbay.domain.auction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
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
