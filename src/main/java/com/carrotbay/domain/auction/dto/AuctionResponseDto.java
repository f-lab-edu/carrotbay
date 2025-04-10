package com.carrotbay.domain.auction.dto;

import java.time.LocalDateTime;

import com.carrotbay.domain.auction.AuctionStatus;
import com.querydsl.core.annotations.QueryProjection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuctionResponseDto {

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ModifyResponseDto {
		private Long id;
		private String title;
		private String content;
		private String status;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private int minimumPrice;
		private int instantPrice;
		private Long successfulBidderId;
		private Long createdBy;
		private String creator;
	}

	@Getter
	@NoArgsConstructor
	public static class AuctionListResponseDto {
		private Long id;
		private String title;
		private String content;
		private String status;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private Long createdBy;
		private String creator;

		@Builder
		@QueryProjection
		public AuctionListResponseDto(Long id, String title, String content, AuctionStatus status,
			LocalDateTime startDate,
			LocalDateTime endDate, Long createdBy, String creator) {
			this.id = id;
			this.title = title;
			this.content = content;
			this.status = status.getStatus();
			this.startDate = startDate;
			this.endDate = endDate;
			this.createdBy = createdBy;
			this.creator = creator;
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
