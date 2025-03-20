package com.carrotbay.config.auth;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

	private final HttpSession httpSession;
	private final String SESSION_KEY = "USER_ID";

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		boolean isLoginUserAnnotation = parameter.getParameterAnnotation(LoginUser.class) != null;
		return isLoginUserAnnotation;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
		if (httpSession == null) {
			throw new IllegalStateException("세션이 존재하지 않습니다.");
		}
		return httpSession.getAttribute(SESSION_KEY);
	}
}
