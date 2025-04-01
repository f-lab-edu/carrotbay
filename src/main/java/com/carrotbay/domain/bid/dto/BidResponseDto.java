package com.carrotbay.domain.bid.dto;

import java.time.LocalDateTime;

import com.querydsl.core.annotations.QueryProjection;

import lombok.Getter;

public class BidResponseDto {

	@Getter
	public static class BidDetailDto {
		private Long id;
		private int bidPrice;
		private LocalDateTime createdAt;
		private Long auctionId;
		private Long userId;

		@QueryProjection
		public BidDetailDto(Long id, int bidPrice, LocalDateTime createdAt, Long auctionId, Long userId) {
			this.id = id;
			this.bidPrice = bidPrice;
			this.createdAt = createdAt;
			this.auctionId = auctionId;
			this.userId = userId;
		}
	}

	@Getter
	public static class BidListResponseDto {
		private Long id;
		private int bidPrice;
		private LocalDateTime createdAt;
		private Long auctionId;
		private Long userId;

		@QueryProjection
		public BidListResponseDto(Long id, int bidPrice, LocalDateTime createdAt, Long auctionId, Long userId) {
			this.id = id;
			this.bidPrice = bidPrice;
			this.createdAt = createdAt;
			this.auctionId = auctionId;
			this.userId = userId;
		}
	}
}
