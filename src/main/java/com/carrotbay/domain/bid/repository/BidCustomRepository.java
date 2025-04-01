package com.carrotbay.domain.bid.repository;

import java.util.List;

import com.carrotbay.domain.bid.dto.BidResponseDto;

public interface BidCustomRepository {
	BidResponseDto.BidDetailDto findHighestBidByAuctionId(Long auctionId);

	List<BidResponseDto.BidListResponseDto> findBidListByAuctionId(Long auctionId);
}
