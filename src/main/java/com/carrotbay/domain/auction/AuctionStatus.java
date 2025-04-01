package com.carrotbay.domain.auction;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuctionStatus {
	AUCTION("경매 진행 중"),
	CLOSE("경매 완료"),
	CANCEL("경매 취소");

	private final String status;

	public String getStatus() {
		return this.status;
	}

	public static AuctionStatus fromString(String status) {
		for (AuctionStatus auctionStatus : AuctionStatus.values()) {
			if (auctionStatus.getStatus().equals(status)) {
				return auctionStatus;
			}
		}
		throw new IllegalArgumentException("AuctionStatus 변환 : " + status);
	}
}
