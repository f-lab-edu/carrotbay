package com.carrotbay.common.handler.aop;

import com.carrotbay.common.exception.CustomValidationException;
import com.carrotbay.common.exception.ErrorCode;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

@Component
@Aspect // 관점
public class CustomValidationAdvice {

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void postMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public void putMapping() {
	}

	@Around("postMapping() || putMapping()") // joinPoint의 전후 제어
	public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object[] args = proceedingJoinPoint.getArgs();// joinPoint의 매개변수들(그러니까 메소드의 매개변수들)
		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;

				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for (FieldError fieldError : bindingResult.getFieldErrors()) {
						errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
					}

					throw new CustomValidationException(ErrorCode.INVALID_PARAMETER, errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed(); // 정상적으로 해당 메소드를 구현해라.
	}
}
