package com.carrotbay.domain.auction.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundAuctionException extends RuntimeException {
	public NotFoundAuctionException(String message) {
		super(message);
	}

	public NotFoundAuctionException(String message, Throwable cause) {
		super(message, cause);
	}

	public NotFoundAuctionException(Throwable cause) {
		super(cause);
	}
}
