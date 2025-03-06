package com.carrotbay.domain.auction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum AuctionStatus {
	AUCTION("경매 진행 중"),
	CLOSE("경매 완료"),
	CANCEL("경매 취소");

	private final String status;

	AuctionStatus(String status) {
		this.status = status;
	}

	@JsonValue
	public String getStatus() {
		return this.status;
	}

	@JsonCreator
	public static AuctionStatus fromString(String status) {
		for (AuctionStatus userStatus : AuctionStatus.values()) {
			if (userStatus.getStatus().equals(status)) {
				return userStatus;
			}
		}
		throw new IllegalArgumentException("AuctionStatus 변환 : " + status);
	}
}
