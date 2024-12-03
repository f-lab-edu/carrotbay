package com.carrotbay.domain.user;

import com.carrotbay.domain.user.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Transactional // 메서드나 클래스에 적용하여, 데이터베이스 작업이 트랜잭션으로 관리되도록 설정하며, 기본적으로 롤백 처리된다.(테스트에서 주로 사용).
@AutoConfigureMockMvc //Spring Boot 테스트 환경에서 MockMvc를 자동으로 구성하여 컨트롤러 테스트를 지원하는 어노테이션.
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) //Spring Boot 애플리케이션 테스트 컨텍스트를 생성하며, 내장 웹 서버 없이(Mock 환경) 테스트를 실행.
class UserControllerTest {
	@Autowired //Spring 컨텍스트에서 관리되는 빈(Bean)을 자동으로 주입해주는 어노테이션.
	MockMvc mvc;
	@MockitoBean //특정 빈을 목(Mock) 객체로 교체하여 테스트에서 외부 의존성을 제거하고, 동작을 모의(mocking)하기 위해 사용.
	UserService userService;
	@Autowired
	ObjectMapper om;

	@Test
	@DisplayName("회원가입 성공케이스.")
	void 회원가입() throws Exception {

		//given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");
		registerUserDto.setNickname("test");
		registerUserDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(registerUserDto);

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		// when, then
		when( userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/registry")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().isCreated());
	}


	@DisplayName("비밀번호에 문자만 존재하면 회원가입에 실패한다.")
	@Test
	void 비밀번호_유효성검사에_실패하는_케이스() throws Exception {

		//given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test");
		registerUserDto.setNickname("test");
		registerUserDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(registerUserDto);

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		// when, then
		when( userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/registry")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.password")
				.value("8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다.")); // password 조건 메시지 검증
	}

	@DisplayName("비밀번호에 숫자만 존재하면 회원가입에 실패한다.")
	@Test
	void 비밀번호_유효성검사에_실패하는_케이스2() throws Exception {

		//given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("12111111111");
		registerUserDto.setNickname("test");
		registerUserDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(registerUserDto);

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		// when, then
		when( userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/registry")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.password")
				.value("8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다.")); // password 조건 메시지 검증
	}

	@DisplayName("비밀번호에 특수문자가 없다면 회원가입에 실패한다.")
	@Test
	void 비밀번호_유효성검사에_실패하는_케이스3() throws Exception {

		//given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test12312");
		registerUserDto.setNickname("test");
		registerUserDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(registerUserDto);

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		// when, then
		when( userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/registry")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.password")
				.value("8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다.")); // password 조건 메시지 검증
	}

	@DisplayName("아아디가 이메일 형식이 아니면 회원가입에 실패한다.")
	@Test
	void 아아디_유효성검사에_실패하는_케이스() throws Exception {

		//given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");
		registerUserDto.setNickname("test");
		registerUserDto.setUsername("test");
		String requestBody = om.writeValueAsString(registerUserDto);

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		// when, then
		when( userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/registry")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.username")
				.value("이메일을 입력해주세요.")); // password 조건 메시지 검증
	}

	@DisplayName("닉네임에 특수문자가 있으면 회원가입에 실패한다.")
	@Test
	void 닉네임_유효성검사에_실패하는_케이스() throws Exception {

		//given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");
		registerUserDto.setNickname("1!@!#");
		registerUserDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(registerUserDto);

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		// when, then
		when( userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/registry")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname")
				.value("1~10자 사이의 한글 및 숫자를 입력해주세요.")); // password 조건 메시지 검증
	}

	@DisplayName("닉네임이 10글자 이상이면 회원가입에 실패한다.")
	@Test
	void 닉네임_유효성검사에_실패하는_케이스2() throws Exception {

		//given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");
		registerUserDto.setNickname("testestestestesetsetsetsetsetset");
		registerUserDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(registerUserDto);

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		// when, then
		when( userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/registry")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname")
				.value("1~10자 사이의 한글 및 숫자를 입력해주세요.")); // password 조건 메시지 검증
	}


	@DisplayName("닉네임이 10글자 이상이면 닉네임중복검사에 실패한다.")
	@Test
	void 닉네임중복검사_실패케이스() throws Exception {

		//given
		UserDto.NicknameDto nicknameDto = new UserDto.NicknameDto();
		nicknameDto.setNickname("test123123123123123");
		String requestBody = om.writeValueAsString(nicknameDto);

		// when, then
		when( userService.duplicateCheckNickname(any())).thenReturn(false);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/nickname")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname")
				.value("1~10자 사이의 한글 및 숫자를 입력해주세요.")); // password 조건 메시지 검증
	}

	@DisplayName("닉네임이 null이면 닉네임중복검사에 실패한다.")
	@Test
	void 닉네임중복검사_실패케이스2() throws Exception {

		//given
		UserDto.NicknameDto nicknameDto = new UserDto.NicknameDto();
		nicknameDto.setNickname(null);
		String requestBody = om.writeValueAsString(nicknameDto);

		// when, then
		when( userService.duplicateCheckNickname(any())).thenReturn(false);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/nickname")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(402)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data.nickname")
				.value("닉네임을 입력해주세요.")); // password 조건 메시지 검증
	}

	@Test
	@DisplayName("로그인 성공케이스.")
	void 로그인_성공케이스() throws Exception {

		//given
		UserDto.LoginRequestDto loginRequestDto = new UserDto.LoginRequestDto();
		loginRequestDto.setPassword("test1234T@");
		loginRequestDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(loginRequestDto);

		// when, then
		when( userService.login(any())).thenReturn(1L);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/login")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
			.andExpect(request().sessionAttribute("USER_ID", 1L));
	}

	@Test
	@DisplayName("이미 로그인을 한 사용자(세션이 존재하는 사용자)의 경우 로그인 요청이 실패한다.")
	void 로그인_실패케이스() throws Exception {

		//given
		UserDto.LoginRequestDto loginRequestDto = new UserDto.LoginRequestDto();
		loginRequestDto.setPassword("test1234T@");
		loginRequestDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(loginRequestDto);

		// 이미 로그인한 상태를 모킹
		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute("USER_ID", 1L);

		// when, then
		mvc
			.perform(MockMvcRequestBuilders.post("/api/user/login")
				.content(requestBody)
				.session(httpSession)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.code").value(409)) // code 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("fail")) // msg 검증
			.andExpect(MockMvcResultMatchers.jsonPath("$.data")
				.value("이미 로그인한 사용자입니다.")); // password 조건 메시지 검증

	}
}