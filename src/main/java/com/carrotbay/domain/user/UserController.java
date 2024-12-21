package com.carrotbay.domain.user;

import com.carrotbay.domain.user.dto.UserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
	private final HttpSession httpSession;
	private static String SESSION_KEY = "USER_ID";
	private static int SESSEION_TIME = 60 * 30;

	@PostMapping("") // HTTP POST 요청을 처리하며, URL 경로가 "/registry"인 요청을 해당 메소드가 처리하도록 지정하는 어노테이션
	public ResponseEntity<?> register(
		@RequestBody // HTTP 요청 본문에 포함된 데이터를 Java 객체로 변환하여 메소드 파라미터로 전달하는 어노테이션
		@Valid // 객체나 메소드 파라미터에 대해 유효성 검사를 수행하도록 하며, 검증 실패 시 예외를 발생시키는 어노테이션
		UserDto.RegisterUserDto registerUserDto, BindingResult bindingResult) {

		UserDto.RegisterUserResponseDto userResponseDto = userService.registerUser(registerUserDto);
		return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
	}

	@PostMapping("/nickname/check")
	public ResponseEntity<?> checkNickname(@RequestBody @Valid UserDto.NicknameDto nicknameDto, BindingResult bindingResult) {
		boolean result = userService.duplicatedCheckNickname(nicknameDto);
		return new ResponseEntity<>(result, HttpStatus.OK);
	}

	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody @Valid UserDto.LoginRequestDto loginRequestDto, HttpServletRequest httpServletRequest, BindingResult bindingResult){

		// 회원 존재 여부 확인 및 세션 생성
		Long userId = userService.login(httpServletRequest.getSession(false), loginRequestDto);
		httpSession.setAttribute(SESSION_KEY, userId);
		httpSession.setMaxInactiveInterval(SESSEION_TIME);
		return new ResponseEntity<>(userId, HttpStatus.OK);
	}

	@GetMapping("/logout")
	public ResponseEntity<?> logout(HttpSession httpSession) {
			httpSession.removeAttribute(SESSION_KEY);
			return new ResponseEntity<>(null, HttpStatus.OK);
	}
}
