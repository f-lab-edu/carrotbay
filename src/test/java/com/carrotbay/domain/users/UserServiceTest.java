package com.carrotbay.domain.users;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.carrotbay.common.exception.CustomApiException;
import com.carrotbay.domain.users.dto.UserDto;
import com.carrotbay.domain.users.repository.UserRepository;
import com.carrotbay.dummy.DummyObject;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Mockito를 사용한 단위 테스트에서 Mockito 확장을 활성화하여 Mock 객체 주입 등을 지원함.
class UserServiceTest extends DummyObject {
	@InjectMocks // Mock 객체를 주입하여 테스트 대상 클래스의 인스턴스를 생성하고 의존성을 자동으로 주입.
	private UserService userService;

	@Mock // Mock 객체를 생성하여 테스트에 사용할 가짜 객체를 제공
	private UserRepository userRepository;

	@Spy //실제 객체를 생성하지만, 필요에 따라 특정 메서드만 Stub(가짜 동작)으로 설정하여 일부 동작을 Mocking할 수 있도록 지원
	private BCryptPasswordEncoder passwordEncoder;

	@Test // 데스트 메서드임을 표시하여 JUnit이 이를 실행 대상으로 간주하도록 설정.
	@DisplayName("회원가입 DTO가 null이면 회원가입에 실패한다.") // 테스트에 이름을 지정하여 가독성을 높이고 테스트 실행결과에 커스텀 설명을 제공.
	void DTO가_null인_경우_회원가입_실패(){
		// given
		UserDto.RegisterUserDto registerUserDto = null;
		//when & then
		assertThrows(NullPointerException.class, ()->userService.registerUser(registerUserDto));
	}

	@Test
	@DisplayName("동일한 닉네임 존재하면 회원가입에 실패한다.")
	void 닉네임_중복_실패_케이스(){
		// given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");
		User user = newMockUser(1L, "test");
		when(userRepository.findByNickname(any())).thenReturn(Optional.ofNullable(user));

		//when & then
		CustomApiException exception = assertThrows(CustomApiException.class, ()->userService.registerUser(registerUserDto));
		assertEquals("이미 해당 닉네임이 존재합니다.", exception.getErrorCode().getMessage());
	}

	@Test
	@DisplayName("동일한 회원이 존재하면 회원가입에 실패한다.")
	void 동일한_회원_존재시_실패_케이스(){
		// given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");
		User user = newMockUser(1L, "test");
		when(userRepository.findByNickname(any())).thenReturn(Optional.empty());
		when(userRepository.findByUsername(any())).thenReturn(Optional.ofNullable(user));

		//when & then
		CustomApiException exception = assertThrows(CustomApiException.class, ()->userService.registerUser(registerUserDto));
		assertEquals("해당 사용자가 이미 존재합니다.", exception.getErrorCode().getMessage());
	}

	@Test
	@DisplayName("회원가입에 성공한다.")
	void 회원가입_성공케이스() {

		// given
		UserDto.RegisterUserDto registerUserDto = new UserDto.RegisterUserDto();
		registerUserDto.setPassword("test1234T@");

		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
		User user = newMockUser(1L, "test");
		when(userRepository.save(any())).thenReturn(user);

		//when
		UserDto.RegisterUserResponseDto joinResponseDto = userService.registerUser(registerUserDto);

		// then
		org.assertj.core.api.Assertions.assertThat(joinResponseDto.getId()).isEqualTo(1L);
		org.assertj.core.api.Assertions.assertThat(joinResponseDto.getUsername()).isEqualTo("test@naver.com");
		Assertions.assertThat(joinResponseDto.getNickname()).isEqualTo("test");
	}
}