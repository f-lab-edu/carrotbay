package com.carrotbay.domain.bid.repository;

import static com.carrotbay.domain.bid.QBid.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.carrotbay.domain.bid.dto.BidResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BidCustomRepositoryImpl implements BidCustomRepository {

	private final JPAQueryFactory query;

	@Override
	public BidResponseDto.BidDetailDto findHighestBidByAuctionId(Long auctionId) {
		return query.select(
				Projections.constructor(
					BidResponseDto.BidDetailDto.class,
					bid.id,
					bid.bidPrice,
					bid.createdAt,
					bid.auction.id,
					bid.user.id))
			.from(bid)
			.where(bid.auction.id.eq(auctionId).and(bid.isDeleted.eq(false)))
			.orderBy(bid.bidPrice.desc())
			.fetchFirst();
	}

	@Override
	public List<BidResponseDto.BidListResponseDto> findBidListByAuctionId(Long auctionId) {
		return query.select(
				Projections.constructor(
					BidResponseDto.BidListResponseDto.class,
					bid.id,
					bid.bidPrice,
					bid.createdAt,
					bid.auction.id,
					bid.user.id))
			.from(bid)
			.where(bid.auction.id.eq(auctionId).and(bid.isDeleted.eq(false)))
			.orderBy(bid.bidPrice.desc())
			.fetch();
	}
}
