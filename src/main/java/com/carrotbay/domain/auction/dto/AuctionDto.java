package com.carrotbay.domain.auction.dto;

import java.time.LocalDateTime;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.AuctionStatus;
import com.carrotbay.domain.user.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class AuctionDto {

	@Getter
	@Builder
	@AllArgsConstructor // 해당 클래스에 있는 모든 필드를 초기화하는 생성자를 자동으로 생성한다.
	@NoArgsConstructor
	public static class CreateAuctionDto {
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
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ModifyAuctionDto {
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
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class ModifyAuctionResponseDto {
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
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class AuctionResponseDto {
		private Long id;
		private String title;
		private String content;
		private String status;
		private LocalDateTime startDate;
		private LocalDateTime endDate;
		private Long createdBy;
		private String creator;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class PostAuctionResponseDto {
		private Long id;
	}

	@Getter
	@Builder
	@AllArgsConstructor
	@NoArgsConstructor
	public static class DeleteAuctionResponseDto {
		private boolean isDeleted;
	}
}
