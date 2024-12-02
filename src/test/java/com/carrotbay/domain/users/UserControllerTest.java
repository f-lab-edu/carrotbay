package com.carrotbay.domain.users;

import com.carrotbay.domain.users.dto.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class UserControllerTest {
	@Autowired
	MockMvc mvc;
	@MockitoBean
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
}