package com.carrotbay.domain.bid.repository;

import static com.carrotbay.domain.bid.QBid.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.carrotbay.domain.bid.dto.BidDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BidCustomRepositoryImpl implements BidCustomRepository {

	private final JPAQueryFactory query;

	@Override
	public BidDto.BidResponseDto findHighestBidByAuctionId(Long auctionId) {
		return query.select(
				Projections.constructor(
					BidDto.BidResponseDto.class,
					bid.id,
					bid.bidPrice,
					bid.createdAt,
					bid.auction.id,
					bid.createdBy.id
				)
			).from(bid)
			.where(bid.auction.id.eq(auctionId).and(bid.isDelete.eq(false)))
			.orderBy(bid.bidPrice.desc())
			.fetchFirst();
	}

	@Override
	public List<BidDto.BidResponseDto> findBidListByAuctionId(Long auctionId) {
		return query.select(
				Projections.constructor(
					BidDto.BidResponseDto.class,
					bid.id,
					bid.bidPrice,
					bid.createdAt,
					bid.auction.id,
					bid.createdBy.id
				)
			).from(bid)
			.where(bid.auction.id.eq(auctionId).and(bid.isDelete.eq(false)))
			.orderBy(bid.bidPrice.desc())
			.fetch();
	}
}
