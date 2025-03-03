package com.carrotbay.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
public class CustomValidationException extends RuntimeException {
	private Map<String, String> errorMap;
}
