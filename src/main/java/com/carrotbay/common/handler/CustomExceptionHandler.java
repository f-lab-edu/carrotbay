package com.carrotbay.common.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.carrotbay.common.exception.CustomValidationException;

import lombok.extern.slf4j.Slf4j;

// Lombok에서 제공하는 어노테이션으로, 클래스에 logger 객체를 자동으로 추가하여 로그를 간편하게 출력할 수 있게 해준다.
@Slf4j
// Spring에서 제공하는 어노테이션으로, @ControllerAdvice와 @ResponseBody의 기능을 결합하여, 예외 처리나 전역적인 응답 처리를 REST API에서 사용할 수 있게 해준다.
@RestControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<?> apiException(RuntimeException exception) {
		log.error(exception.getMessage());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(exception.getMessage());
	}

	@ExceptionHandler(CustomValidationException.class)
	public ResponseEntity<?> validationException(CustomValidationException exception) {
		log.error(exception.getMessage());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(exception.getErrorMap());
	}
}