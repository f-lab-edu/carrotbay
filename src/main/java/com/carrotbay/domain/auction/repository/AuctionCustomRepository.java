package com.carrotbay.domain.auction.repository;

import java.util.List;

import com.carrotbay.domain.auction.dto.AuctionDto;

public interface AuctionCustomRepository {
	public List<AuctionDto.AuctionResponseDto> findAuctionList();
}
