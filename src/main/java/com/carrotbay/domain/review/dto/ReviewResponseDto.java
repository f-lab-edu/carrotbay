package com.carrotbay.domain.review.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewResponseDto {

	@Getter
	@NoArgsConstructor
	public static class ModifyResponseDto {
		private Long id;
		private String title;
		private String content;
		private LocalDateTime createdAt;
		private Long auctionId;
		private Long userId;
		private String nickName;
		private String auctionTitle;

		@Builder
		public ModifyResponseDto(Long id, String title, String content, LocalDateTime createdAt, Long auctionId,
			Long userId, String nickName, String auctionTitle) {
			this.id = id;
			this.title = title;
			this.content = content;
			this.createdAt = createdAt;
			this.auctionId = auctionId;
			this.userId = userId;
			this.nickName = nickName;
			this.auctionTitle = auctionTitle;
		}
	}

	@Getter
	public static class ListResponseDto {
		private Long id;
		private String title;
		private String content;
		private LocalDateTime createdAt;
		private Long auctionId;
		private Long userId;
		private String nickName;
		private String auctionTitle;

		@Builder
		public ListResponseDto(Long id, String title, String content, LocalDateTime createdAt, Long auctionId,
			Long userId, String nickName, String auctionTitle) {
			this.id = id;
			this.title = title;
			this.content = content;
			this.createdAt = createdAt;
			this.auctionId = auctionId;
			this.userId = userId;
			this.nickName = nickName;
			this.auctionTitle = auctionTitle;
		}
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PostResponseDto {
		private Long id;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DeleteResponseDto {
		private boolean isDeleted;
	}
}
