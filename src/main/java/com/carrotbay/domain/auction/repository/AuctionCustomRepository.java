package com.carrotbay.domain.auction.repository;

import java.util.List;
import java.util.Optional;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.dto.AuctionResponseDto;

public interface AuctionCustomRepository {
	public List<AuctionResponseDto.AuctionListResponseDto> findAuctionList();

	public Optional<Auction> getAuctionWithLock(Long auctionId);
}
