package com.carrotbay.domain.auction.repository;

import static com.carrotbay.domain.auction.QAuction.*;
import static com.carrotbay.domain.user.QUser.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.carrotbay.domain.auction.dto.AuctionDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuctionCustomRepositoryImpl implements AuctionCustomRepository {

	private final JPAQueryFactory query;

	@Override
	public List<AuctionDto.AuctionResponseDto> findAuctionList() {
		return query.select(
				Projections.constructor(AuctionDto.AuctionResponseDto.class,
					auction.id,
					auction.title,
					auction.content,
					auction.status,
					auction.createdAt,
					auction.endDate,
					auction.createdBy.id,
					auction.createdBy.nickname
				)
			).from(auction)
			.leftJoin(user).on(user.id.eq(auction.createdBy.id))
			.fetch();
	}
}
