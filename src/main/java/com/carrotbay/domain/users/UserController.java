package com.carrotbay.domain.users;

import com.carrotbay.common.exception.CustomApiException;
import com.carrotbay.common.dto.HttpResponseDto;
import com.carrotbay.domain.users.dto.UserDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/user") // "/api/user" 경로로 들어오는 요청을 해당 메서드로 매핑하는 Spring MVC의 요청 처리 어노테이션
@RequiredArgsConstructor
@RestController // Spring MVC에서 컨트롤러 역할을 하는 클래스에 사용되는 어노테이션, 웹 요청을 처리하고, 요청에 맞는 뷰(웹 페이지)를 반환하는 역할함.
public class UserController {

	private final UserService userService;

	/**
	 * 회원가입
	 * @param registerUserDto
	 * @return
	 */
	@PostMapping("/registry") // HTTP POST 요청을 처리하며, URL 경로가 "/registry"인 요청을 해당 메소드가 처리하도록 지정하는 어노테이션
	public ResponseEntity<?> register(
		@RequestBody // HTTP 요청 본문에 포함된 데이터를 Java 객체로 변환하여 메소드 파라미터로 전달하는 어노테이션
		@Valid // 객체나 메소드 파라미터에 대해 유효성 검사를 수행하도록 하며, 검증 실패 시 예외를 발생시키는 어노테이션
		UserDto.RegisterUserDto registerUserDto, BindingResult bindingResult) {
		try {
			UserDto.RegisterUserResponseDto userResponseDto = userService.registerUser(registerUserDto);
			return new ResponseEntity<>(new HttpResponseDto<>(200, "회원가입 성공", userResponseDto), HttpStatus.CREATED);

		} catch (CustomApiException e) {
			return new ResponseEntity<>(
				new HttpResponseDto<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), e.getMessage()),
				HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(
				new HttpResponseDto<>(e.hashCode(), "서버 오류가 발생했습니다. 개발팀에 문의해주세요..", e.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/**
	 * 닉네임 중복 검사
	 * @param nickname
	 * @return
	 */
	@PostMapping("/nickname")
	public ResponseEntity<?> checkNickname(@RequestParam(name = "nickname") @Valid String nickname) {
		try {
			boolean result = userService.checkNickname(nickname);
			return new ResponseEntity<>(new HttpResponseDto<>(200, "닉네임 중복 검사 성공", result), HttpStatus.CREATED);

		} catch (CustomApiException e) {
			return new ResponseEntity<>(
				new HttpResponseDto<>(e.getErrorCode().getStatus(), e.getErrorCode().getMessage(), e.getMessage()),
				HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			return new ResponseEntity<>(
				new HttpResponseDto<>(e.hashCode(), "서버 오류가 발생했습니다. 개발팀에 문의해주세요.", e.getMessage()),
				HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}