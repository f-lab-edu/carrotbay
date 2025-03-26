package com.carrotbay.domain.bid.dto;

import java.time.LocalDateTime;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.bid.Bid;
import com.carrotbay.domain.bid.BidStatus;
import com.carrotbay.domain.user.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BidDto {

	@Getter
	@NoArgsConstructor
	public static class CreateBidDto {
		@Min(value = 1, message = "입찰 가격은 0원이 될 수 없습니다.")
		private int bidPrice;

		public Bid toEntity(User user, Auction auction) {
			return Bid.builder()
				.bidPrice(this.bidPrice)
				.status(BidStatus.BID)
				.isDelete(false)
				.createdBy(user)
				.auction(auction)
				.build();
		}

		public CreateBidDto(int bidPrice) {
			this.bidPrice = bidPrice;
		}
	}

	@Getter
	public static class BidResponseDto {
		private Long id;
		private int bidPrice;
		private LocalDateTime createdAt;
		private Long auctionId;
		private Long userId;

		public BidResponseDto(Long id, int bidPrice, LocalDateTime createdAt, Long auctionId,
			Long userId) {
			this.id = id;
			this.bidPrice = bidPrice;
			this.createdAt = createdAt;
			this.auctionId = auctionId;
			this.userId = userId;
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class BidCancelRequestDto {
		@NotNull(message = "경매 id를 입력해주세요.")
		private Long auctionId;
	}
}
