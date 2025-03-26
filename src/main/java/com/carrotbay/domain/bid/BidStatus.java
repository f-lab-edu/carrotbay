package com.carrotbay.domain.bid;

public enum BidStatus {
	BID("입찰"),
	CANCEL("취소");

	private final String status;

	BidStatus(String status) {
		this.status = status;
	}

	public String getStatus() {
		return this.status;
	}

	public static BidStatus fromString(String status) {
		for (BidStatus bidStatus : BidStatus.values()) {
			if (bidStatus.getStatus().equals(status)) {
				return bidStatus;
			}
		}
		throw new IllegalArgumentException("BidStatus 변환 : " + status);
	}
}
