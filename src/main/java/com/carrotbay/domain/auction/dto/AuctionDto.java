package com.carrotbay.domain.auction.dto;

import java.time.LocalDateTime;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.AuctionStatus;
import com.carrotbay.domain.user.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class AuctionDto {

	@Getter
	@Setter
	public static class UpsertAuctionDto {
		@NotEmpty(message = "제목을 입력해주세요.")
		@Size(min = 5, max = 20, message = "제목은 5자 이상 20자 이하로 입력해주세요.")
		private String title;

		@NotEmpty(message = "본문을 입력해주세요.")
		private String content;

		@NotNull(message = "경매 종료날짜를 입력해주세요.")
		private LocalDateTime endDate;

		@Min(value = 1, message = "최소 가격은 0원이 될 수 없습니다.")
		private int minimumPrice;

		@Min(value = 1, message = "즉시 낙찰 가격은 0원이 될 수 없습니다.")
		private int instantPrice;

		public Auction toEntity(User createdBy) {
			return Auction.builder()
				.title(this.title)
				.content(this.content)
				.status(AuctionStatus.AUCTION)
				.endDate(this.endDate)
				.minimumPrice(this.minimumPrice)
				.instantPrice(this.instantPrice)
				.createdBy(createdBy)
				.build();
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class AuctionResponseDto {
		private Long id;
		private String title;
		private String content;
		private String status;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private int minimumPrice;
		private int instantPrice;
		private Long successfulBidderId;
		private String createdBy;

		public AuctionResponseDto(Long id, String title, String content, AuctionStatus status, LocalDateTime startDate,
			LocalDateTime endDate, int minimumPrice, int instantPrice, Long successfulBidderId, String createdBy) {
			this.id = id;
			this.title = title;
			this.content = content;
			this.status = status.getStatus();
			this.startDate = startDate;
			this.endDate = endDate;
			this.minimumPrice = minimumPrice;
			this.instantPrice = instantPrice;
			this.successfulBidderId = successfulBidderId;
			this.createdBy = createdBy;
		}

		public AuctionResponseDto(Auction auction) {
			this.id = auction.getId();
			this.title = auction.getTitle();
			this.content = auction.getContent();
			this.status = auction.getStatus().getStatus();
			this.endDate = auction.getEndDate();
			this.minimumPrice = auction.getMinimumPrice();
			this.instantPrice = auction.getInstantPrice();
			this.startDate = auction.getCreatedAt();
			if (auction.getSuccessfulBidder() != null) {
				this.successfulBidderId = auction.getSuccessfulBidder().getId();
				this.createdBy = auction.getSuccessfulBidder().getNickname();
			}
		}
	}

	@Getter
	@Setter
	@NoArgsConstructor
	public static class AuctionRequestDto {
		private Long auctionId;
		private String title;
		private LocalDateTime startDate;
		private LocalDateTime createdBy;
		private int pageNumber;
		private int pageSize;

		public AuctionRequestDto(Long auctionId, String title, LocalDateTime startDate, LocalDateTime createdBy,
			int pageNumber, int pageSize) {
			this.auctionId = auctionId;
			this.title = title;
			this.startDate = startDate;
			this.createdBy = createdBy;
			this.pageNumber = pageNumber;
			this.pageSize = pageSize;
		}
	}
}
