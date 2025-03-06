package com.carrotbay.domain.user;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

import com.carrotbay.domain.user.dto.UserDto;
import com.carrotbay.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional // 메서드나 클래스에 적용하여, 데이터베이스 작업이 트랜잭션으로 관리되도록 설정하며, 기본적으로 롤백 처리된다.(테스트에서 주로 사용).
@AutoConfigureMockMvc //Spring Boot 테스트 환경에서 MockMvc를 자동으로 구성하여 컨트롤러 테스트를 지원하는 어노테이션.
@SpringBootTest//Spring Boot 애플리케이션 테스트 컨텍스트를 생성하며, 내장 웹 서버 없이(Mock 환경) 테스트를 실행.
class UserControllerTest {
	@Autowired //Spring 컨텍스트에서 관리되는 빈(Bean)을 자동으로 주입해주는 어노테이션.
	private MockMvc mvc;
	@MockitoBean //특정 빈을 목(Mock) 객체로 교체하여 테스트에서 외부 의존성을 제거하고, 동작을 모의(mocking)하기 위해 사용.
	private UserService userService;
	@MockitoBean
	private UserRepository userRepository;
	@Autowired
	private ObjectMapper om;

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
		when(userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	@DisplayName("이미 존재하는 사용자로 회원가입을 시도하면 실패한다.")
	void 회원가입_실패케이스() throws Exception {

		// given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");
		registerUserDto.setNickname("test");
		registerUserDto.setUsername("test@naver.com");

		UserDto.RegisterUserResponseDto responseDto = new UserDto.RegisterUserResponseDto();
		responseDto.setId(1L);
		responseDto.setNickname("test");
		responseDto.setUsername("test@naver.com");

		when(userService.registerUser(any())).thenThrow(new IllegalArgumentException("해당 사용자가 이미 존재합니다."));
		String requestBody = om.writeValueAsString(registerUserDto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isBadRequest())
			.andExpect(MockMvcResultMatchers.content().string("해당 사용자가 이미 존재합니다."));
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
		when(userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.password")
				.value("8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다."));
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
		when(userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.password")
				.value("8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다."));
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
		when(userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.password")
				.value("8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다."));
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
		when(userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.username")
				.value("이메일을 입력해주세요."));
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
		when(userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.nickname")
				.value("1~10자 사이의 한글 및 숫자를 입력해주세요."));
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
		when(userService.registerUser(any())).thenReturn(responseDto);
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.nickname")
				.value("1~10자 사이의 한글 및 숫자를 입력해주세요."));
	}

	@DisplayName("닉네임이 10글자 이상이면 닉네임중복검사에 실패한다.")
	@Test
	void 닉네임중복검사_실패케이스() throws Exception {

		//given
		UserDto.NicknameDto nicknameDto = new UserDto.NicknameDto();
		nicknameDto.setNickname("test123123123123123");
		String requestBody = om.writeValueAsString(nicknameDto);

		// when, then
		when(userService.duplicatedCheckNickname(any())).thenReturn(false);
		mvc
			.perform(MockMvcRequestBuilders.put("/api/users/check")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.nickname")
				.value("1~10자 사이의 한글 및 숫자를 입력해주세요."));
	}

	@DisplayName("닉네임이 null이면 닉네임중복검사에 실패한다.")
	@Test
	void 닉네임중복검사_실패케이스2() throws Exception {

		//given
		UserDto.NicknameDto nicknameDto = new UserDto.NicknameDto();
		nicknameDto.setNickname(null);
		String requestBody = om.writeValueAsString(nicknameDto);

		// when, then
		when(userService.duplicatedCheckNickname(any())).thenReturn(false);
		mvc
			.perform(MockMvcRequestBuilders.put("/api/users/check")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.nickname")
				.value("닉네임을 입력해주세요."));
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
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users/login")
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

		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute("USER_ID", 1L);

		when(userService.login(any(), any())).thenThrow(new IllegalArgumentException("이미 로그인 상태입니다."));
		// when, then
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users/login")
				.content(requestBody)
				.session(httpSession)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.content().string("이미 로그인 상태입니다."));
	}

	@Test
	@DisplayName("해당 사용자가 존재하지않는 경우에는 로그인에 실패한다.")
	void 로그인_실패케이스_사용자가_이미_존재() throws Exception {

		//given
		UserDto.LoginRequestDto loginRequestDto = new UserDto.LoginRequestDto();
		loginRequestDto.setPassword("test1234T@");
		loginRequestDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(loginRequestDto);

		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute("USER_ID", 1L);

		when(userService.login(any(), any())).thenThrow(new IllegalArgumentException("해당 사용자가 존재하지않습니다."));
		// when, then
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users/login")
				.content(requestBody)
				.session(httpSession)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.content().string("해당 사용자가 존재하지않습니다."));
	}

	@Test
	@DisplayName("비밀번호가 일치하지않는 경우에는 로그인에 실패한다.")
	void 로그인_실패케이스_비밀번호가_일치하지않음() throws Exception {

		//given
		UserDto.LoginRequestDto loginRequestDto = new UserDto.LoginRequestDto();
		loginRequestDto.setPassword("test1234T@");
		loginRequestDto.setUsername("test@naver.com");
		String requestBody = om.writeValueAsString(loginRequestDto);

		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute("USER_ID", 1L);

		when(userService.login(any(), any())).thenThrow(new IllegalArgumentException("비밀번호가 일치하지않습니다."));
		// when, then
		mvc
			.perform(MockMvcRequestBuilders.post("/api/users/login")
				.content(requestBody)
				.session(httpSession)
				.contentType(MediaType.APPLICATION_JSON)
			)
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.content().string("비밀번호가 일치하지않습니다."));
	}
}