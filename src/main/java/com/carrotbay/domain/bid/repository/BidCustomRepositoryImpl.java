package com.carrotbay.domain.bid.repository;

import static com.carrotbay.domain.bid.QBid.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.carrotbay.domain.bid.Bid;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BidCustomRepositoryImpl implements BidCustomRepository {

	private final JPAQueryFactory query;

	@Override
	public Bid findHighestBidByAuctionId(Long auctionId) {
		return query.select(bid)
			.from(bid)
			.where(bid.auction.id.eq(auctionId).and(bid.isDeleted.eq(false)))
			.orderBy(bid.bidPrice.desc())
			.fetchFirst();
	}

	@Override
	public List<Bid> findBidListByAuctionId(Long auctionId) {
		return query.select(bid)
			.from(bid)
			.where(bid.auction.id.eq(auctionId).and(bid.isDeleted.eq(false)))
			.orderBy(bid.bidPrice.desc())
			.fetch();
	}
}
