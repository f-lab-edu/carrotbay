package com.carrotbay.domain.bid.repository;

import java.util.List;

import com.carrotbay.domain.bid.Bid;

public interface BidCustomRepository {
	Bid findHighestBidByAuctionId(Long auctionId);

	List<Bid> findBidListByAuctionId(Long auctionId);
}
