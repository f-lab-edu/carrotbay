package com.carrotbay.domain.user;

import com.carrotbay.common.exception.CustomApiException;
import com.carrotbay.common.exception.ErrorCode;
import com.carrotbay.domain.user.dto.UserDto;
import com.carrotbay.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor // final 또는 @NonNull이 붙은필드에 대한 생성자를 자동으로 생성해줌
@Service
//Spring 의 서비스 레이어를 정의하는 어노테이션. 일반적으로 비즈니스 로직을 처리하는 클래스에 붙이며 Spring 의 빈으로 등록되어 의존성을 주입받거나 서비스 메서드를 외부에서 호출 할 수있게 됨.
public class UserService {
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder;

	/**
	 * 회원 등록
	 */
	public UserDto.RegisterUserResponseDto registerUser(UserDto.RegisterUserDto registerUserDto) {

		// 닉네임 중복 검사
		checkNickname(registerUserDto.getNickname());
		// 동일 유저네임 존재 여부 검사
		Optional<User> userOptional = userRepository.findByUsername(registerUserDto.getUsername());
		if (userOptional.isPresent()) {
			throw new CustomApiException(ErrorCode.USER_EXIST);
		}
		// 패스워드 인코딩
		User persistedUser = userRepository.save(registerUserDto.toEntity(passwordEncoder));
		// dto 응답
		return new UserDto.RegisterUserResponseDto(persistedUser);
	}

	/**
	 * 닉네임 중복 검사
	 *
	 * @param nickname
	 * @return
	 */
	public boolean duplicateCheckNickname(UserDto.NicknameDto nickname) {
		return checkNickname(nickname.getNickname());
	}

	/**
	 * 로그인 : 해당 사용자가 존재하는 지 확인
	 * @param loginRequestDto
	 * @return
	 */
	public Long login(UserDto.LoginRequestDto loginRequestDto){

		// 사용자 정보 찾기
		User loginUser = userRepository.findByUsername(loginRequestDto.getUsername())
			.orElseThrow(() -> new CustomApiException(ErrorCode.USER_NOT_EXIST));

		// 비밀번호 비교
		if(passwordEncoder.matches(loginRequestDto.getPassword(), loginUser.getPassword())) {
			return loginUser.getId();
		} else {
			// 비밀번호가 일치하지 않으면 별도의 예외 처리
			throw new CustomApiException(ErrorCode.INVALID_PASSWORD);
		}
	}

	public boolean checkNickname(String nickname){
		Optional<User> userOptional = userRepository.findByNickname(nickname);
		if (userOptional.isPresent()) {
			throw new CustomApiException(ErrorCode.NICKNAME_EXIST);
		}
		return true;
	}

}
