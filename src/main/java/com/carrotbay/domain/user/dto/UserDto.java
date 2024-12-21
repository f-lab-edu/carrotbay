package com.carrotbay.domain.user.dto;

import org.mindrot.jbcrypt.BCrypt;

import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class UserDto {

    @Getter // 해당 클래스의 모든 필드에 대해 자동으로 getter 메서드를 생성
    @Setter // 해당 클래스의 모든 필드에 대해 자동으로 setter 메서드를 생성.
    public static class RegisterUserDto{
        @NotEmpty(message = "이메일을 입력해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" , message = "이메일을 입력해주세요.")
        private String username; // 회원 아이디

        @NotEmpty(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "^(?=(.*[a-zA-Z]){1})(?=(.*\\W){1})[a-zA-Z\\W0-9]{8,20}$", message = "8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다.")
        private String password; // 비밀번호

        @NotEmpty(message = "닉네임을 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z가-힣0-9]{1,10}$", message = "1~10자 사이의 한글 및 숫자를 입력해주세요.")
        private String nickname; // 닉네임
        private String introduce; // 자기소개
        private String imageUrl; // 프사 이미지 URL

        public User toEntity(){
            String hashed = BCrypt.hashpw(password, BCrypt.gensalt());
            return User.builder()
                    .username(this.username)
                    .password(hashed)
                    .nickname(this.nickname)
                    .introduce(this.introduce)
                    .imageUrl(this.imageUrl)
                    .status(UserStatus.ACTIVE)
                    .build();
        }
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class RegisterUserResponseDto{
        private Long id;
        private String username;
        private String nickname;

        public RegisterUserResponseDto(User user) {
            this.id = user.getId();
            this.nickname = user.getNickname();
            this.username = user.getUsername();
        }
    }

    @Getter
    @Setter
    public static class NicknameDto{
        @NotEmpty(message = "닉네임을 입력해주세요.")
        @Pattern(regexp = "^[a-zA-Z가-힣0-9]{1,10}$", message = "1~10자 사이의 한글 및 숫자를 입력해주세요.")
        private String nickname; // 닉네임
    }

    @Getter
    @Setter
    public static class LoginRequestDto{
        @NotEmpty(message = "이메일을 입력해주세요")
        @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$" , message = "이메일을 입력해주세요.")
        private String username; // 회원 아이디

        @NotEmpty(message = "비밀번호를 입력해주세요")
        @Pattern(regexp = "^(?=(.*[a-zA-Z]){1})(?=(.*\\W){1})[a-zA-Z\\W0-9]{8,20}$", message = "8~20자 사이에 영대소문자와 특수문자를 하나 이상 포함해야 합니다.")
        private String password; // 비밀번호
    }

}
