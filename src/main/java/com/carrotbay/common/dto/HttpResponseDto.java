package com.carrotbay.common.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
public class HttpResponseDto<T> {
	private final Integer code; // 생성자에서 초기화
	private final String msg;
	private final T data;

	public HttpResponseDto(Integer code, String msg, T data) {
		this.code = code;
		this.msg = msg;
		this.data = data;
	}
}
