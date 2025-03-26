package com.carrotbay.domain.bid.repository;

import java.util.List;

import com.carrotbay.domain.bid.dto.BidDto;

public interface BidCustomRepository {
	BidDto.BidResponseDto findHighestBidByAuctionId(Long auctionId);

	List<BidDto.BidResponseDto> findBidListByAuctionId(Long auctionId);
}
