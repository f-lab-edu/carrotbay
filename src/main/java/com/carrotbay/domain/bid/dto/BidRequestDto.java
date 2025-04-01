package com.carrotbay.domain.bid.dto;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.bid.Bid;
import com.carrotbay.domain.bid.BidStatus;
import com.carrotbay.domain.user.User;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BidRequestDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateBidDto {
		@Min(value = 1, message = "입찰 가격은 0원이 될 수 없습니다.")
		private int bidPrice;

		public Bid toEntity(User user, Auction auction) {
			return Bid.builder()
				.bidPrice(this.bidPrice)
				.status(BidStatus.BID)
				.isDeleted(false)
				.user(user)
				.createdBy(user.getId())
				.auction(auction)
				.build();
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DeleteBidDto {
		@NotNull(message = "경매 id를 입력해주세요.")
		private Long auctionId;
	}
}
