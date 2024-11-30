package com.carrotbay.domain.users;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;

import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@NoArgsConstructor //파라미터가 없는 디폴트 생성자를 생성
@EntityListeners(AuditingEntityListener.class) // Spring Data JPA에서 제공하는 이벤트 리스너로 엔티티의 영속, 수정 이벤트를 감지하는 역할
@Table(name = "users") // 객체 - 테이블 매핑 어노테이션, JPA가 내부적으로 구분하는 이름
@Getter
@Entity // 엔티티임을 알려주는 어노테이션, @Entity가 붙은 클래스는 JPA가 관리한다.
public class User {

	@Id // 기본키 맵핑
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//  데이터베이스의 자동 증가(AUTO_INCREMENT) 기능을 이용해 기본 키 값을 자동으로 생성하도록 설정하는 어노테이션
	private Long id;

	@Column(nullable = false, length = 50) // 해당 필드를 매핑할 데이터베이스 열(column)의 제약 조건을 설정하는 어노테이션
	private String username;

	@Column(nullable = false, length = 500)
	private String password;

	@Column(nullable = false, length = 10)
	private String nickname;

	@Column(name = "image_url")
	private String imageUrl;

	@Column(nullable = false, length = 500)
	private String introduce;

	@Column(nullable = false)
	private UserStatus state;

	@LastModifiedDate //  // Entity가 생성되어 저장될 때 시간이 자동 저장
	@Column(nullable = false, name = "modified_at")
	private LocalDateTime modifiedAt;

	@CreatedDate //  // Entity가 생성되어 저장될 때 시간이 자동 저장
	@Column(nullable = false, name = "created_at")
	private LocalDateTime createdAt;

	@Column(nullable = false, name = "is_delete")
	private boolean isDelete;

	@Builder
	public User(String username, String password, String nickname, String imageUrl, String introduce,
		UserStatus state) {
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.imageUrl = imageUrl;
		this.introduce = introduce;
		this.state = state;
	}
}