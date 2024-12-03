package com.carrotbay.common.handler;

import com.carrotbay.common.dto.HttpResponseDto;
import com.carrotbay.common.exception.CustomApiException;
import com.carrotbay.common.exception.CustomValidationException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(
			new HttpResponseDto<>(e.getErrorCode().getStatus(), "fail", e.getErrorCode().getMessage()),
			HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(CustomValidationException.class)
	public ResponseEntity<?> validationException(CustomValidationException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new HttpResponseDto<>(e.getErrorCode().getStatus(), "fail", e.getErrorMap()),
			HttpStatus.BAD_REQUEST);
	}

}