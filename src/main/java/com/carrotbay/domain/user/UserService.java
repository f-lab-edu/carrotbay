package com.carrotbay.domain.user;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

import com.carrotbay.domain.user.dto.UserDto;
import com.carrotbay.domain.user.repository.UserRepository;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // final 또는 @NonNull이 붙은필드에 대한 생성자를 자동으로 생성해줌
//Spring 의 서비스 레이어를 정의하는 어노테이션. 일반적으로 비즈니스 로직을 처리하는 클래스에 붙이며 Spring 의 빈으로 등록되어 의존성을 주입받거나 서비스 메서드를 외부에서 호출 할 수있게 됨.
@Service
public class UserService {
	private final UserRepository userRepository;

	public UserDto.RegisterUserResponseDto registerUser(UserDto.RegisterUserDto registerUserDto) {
		duplicatedCheckNickname(registerUserDto.getNickname());
		Optional<User> userOptional = userRepository.findByUsername(registerUserDto.getUsername());
		if (userOptional.isPresent()) {
			throw new IllegalArgumentException("해당 사용자가 이미 존재합니다.");
		}
		User persistedUser = userRepository.save(registerUserDto.toEntity());
		return new UserDto.RegisterUserResponseDto(persistedUser);
	}

	public boolean checkNickname(UserDto.NicknameDto nickname) {
		return duplicatedCheckNickname(nickname.getNickname());
	}

	public Long login(HttpSession session, UserDto.LoginRequestDto loginRequestDto) {
		boolean isLoggedIn = session != null;
		if (isLoggedIn) {
			throw new IllegalArgumentException("이미 로그인 상태입니다.");
		}
		User loginUser = userRepository.findByUsername(loginRequestDto.getUsername())
			.orElseThrow(() -> new NullPointerException("해당 사용자가 존재하지않습니다."));
		if (BCrypt.checkpw(loginRequestDto.getPassword(), loginUser.getPassword())) {
			return loginUser.getId();
		} else {
			throw new IllegalArgumentException("비밀번호가 일치하지않습니다.");
		}
	}

	public boolean duplicatedCheckNickname(String nickname) {
		Optional<User> userOptional = userRepository.findByNickname(nickname);
		if (userOptional.isPresent()) {
			throw new IllegalArgumentException("이미 해당 닉네임이 존재합니다.");
		}
		return true;
	}
}
