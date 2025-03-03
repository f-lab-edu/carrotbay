package com.carrotbay.common.handler.aop;

import com.carrotbay.common.exception.CustomValidationException;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.HashMap;
import java.util.Map;

//@Component는 Spring의 컴포넌트 스캔 기능을 통해 해당 클래스를 Spring의 빈(Bean)으로 등록한다.
// 즉, 이 어노테이션이 붙은 클래스는 Spring 컨테이너에서 관리되며, 다른 클래스에서 의존성 주입(Dependency Injection)을 통해 사용할 수 있다.
@Component
//@Aspect는 해당 클래스가 Aspect를 정의하는 클래스임을 나타낸다.
// AOP(Aspect-Oriented Programming)에서 **어드바이스(Advice)**와 **포인트컷(Pointcut)**을 정의하는 데 사용되며
// 이 어노테이션이 붙은 클래스는 특정 메서드나 특정 지점에 추가적인 기능을 삽입할 수 있도록 한다. 예를 들어, 로깅, 트랜잭션 관리 등 공통 관심사를 처리하는 데 사용된다.
@Aspect
public class CustomValidationAdvice {

	//@Pointcut은 AOP에서 어드바이스가 적용될 지점을 정의하는 데 사용된다.
	// 이 어노테이션은 특정 메서드나 클래스에 대해 어느 부분에서 어드바이스를 적용할지 정의한다. @Pointcut은 주로 표현식(예: 메서드 실행, 메서드 호출 등)을 사용하여 지정한다.
	@Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
	public void postMapping() {
	}

	@Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
	public void putMapping() {
	}

	//@Around는 AOP에서 어드바이스의 일종으로, 포인트컷에 해당하는 메서드 호출 전후에 실행되는 로직을 정의한다.
	// @Around 어노테이션이 붙은 메서드는 포인트컷으로 지정된 메서드의 전후에 실행될 수 있으며, ProceedingJoinPoint를 통해 메서드를 실행하고 그 결과를 처리할 수 있다.
	// 주로 메서드 실행 전후에 로깅, 보안 체크, 트랜잭션 처리 등을 할 때 사용된다.
	@Around("postMapping() || putMapping()")
	public Object validationAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		Object[] args = proceedingJoinPoint.getArgs();
		for (Object arg : args) {
			if (arg instanceof BindingResult) {
				BindingResult bindingResult = (BindingResult)arg;

				if (bindingResult.hasErrors()) {
					Map<String, String> errorMap = new HashMap<>();

					for (FieldError fieldError : bindingResult.getFieldErrors()) {
						errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
					}

					throw new CustomValidationException(errorMap);
				}
			}
		}
		return proceedingJoinPoint.proceed();
	}
}
