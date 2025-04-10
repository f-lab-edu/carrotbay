package com.carrotbay.domain.review;

import java.time.LocalDateTime;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "review")
@Getter
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "title", nullable = false)
	private String title;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "is_deleted", nullable = false)
	private boolean isDeleted;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "auction_id")
	private Auction auction;

	@Column(name = "created_by", nullable = false)
	private Long createdBy;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = true)
	private LocalDateTime modifiedAt;

	@Builder
	public Review(Long id, String title, String content, User user, Auction auction, Long createdBy) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.user = user;
		this.auction = auction;
		this.createdBy = createdBy;
	}

	public void update(String title, String content) {
		if (StringUtils.isBlank(title)) {
			this.title = title;
		}
		if (StringUtils.isBlank(content)) {
			this.content = content;
		}
	}

	public void delete() {
		this.isDeleted = true;
	}
}
