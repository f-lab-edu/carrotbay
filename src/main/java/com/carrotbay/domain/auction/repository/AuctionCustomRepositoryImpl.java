package com.carrotbay.domain.auction.repository;

import static com.carrotbay.domain.auction.QAuction.*;
import static com.carrotbay.domain.user.QUser.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.dto.AuctionResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuctionCustomRepositoryImpl implements AuctionCustomRepository {

	private final JPAQueryFactory query;

	@Override
	public List<AuctionResponseDto.AuctionListResponseDto> findAuctionList() {
		return query.select(
				Projections.constructor(AuctionResponseDto.AuctionListResponseDto.class,
					auction.id,
					auction.title,
					auction.content,
					auction.status,
					auction.createdAt,
					auction.endDate,
					auction.user.id,
					auction.user.nickname))
			.from(auction)
			.leftJoin(user).on(user.id.eq(auction.user.id))
			.fetch();
	}

	@Override
	public Optional<Auction> getAuctionWithLock(Long auctionId) {
		return Optional.ofNullable(
			query
				.select(auction)
				.from(auction)
				.where(auction.id.eq(auctionId))
				.setLockMode(LockModeType.PESSIMISTIC_WRITE)
				.fetchOne());
	}

}
