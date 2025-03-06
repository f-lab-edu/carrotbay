package com.carrotbay.domain.auction.repository;

import static com.carrotbay.domain.auction.QAuction.*;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import com.carrotbay.domain.auction.dto.AuctionDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class AuctionCustomRepositoryImpl implements AuctionCustomRepository {
	private final JPAQueryFactory query;

	// 이거 auction.successfulBidder.nickname 이 부분에 대해 생각해봐야함 그냥 JOin하는거랑 뭐가 다른ㄷ지.
	@Override
	public List<AuctionDto.AuctionResponseDto> getAuctionList(AuctionDto.AuctionRequestDto dto) {
		BooleanBuilder builder = getFilter(dto);
		return query.select(
				Projections.constructor(
					AuctionDto.AuctionResponseDto.class,
					auction.id,
					auction.title,
					auction.content,
					auction.status,
					auction.createdAt,
					auction.endDate,
					auction.minimumPrice,
					auction.instantPrice,
					auction.successfulBidder.id,
					auction.successfulBidder.nickname
				)
			).from(auction)
			.where(builder)
			.orderBy(auction.id.desc())
			.offset((long)dto.getPageNumber() * dto.getPageSize())
			.limit(dto.getPageSize())
			.fetch();
	}

	public BooleanBuilder getFilter(AuctionDto.AuctionRequestDto dto) {
		BooleanBuilder builder = new BooleanBuilder();
		if (StringUtils.isBlank(dto.getTitle())) {
			builder.and(auction.title.like("%" + dto.getTitle() + "%"));
		}
		if (dto.getStartDate() != null) {
			builder.and(auction.createdAt.goe(dto.getStartDate()));
		}
		if (dto.getAuctionId() != null) {
			builder.and(auction.id.eq(dto.getAuctionId()));
		}
		return builder;
	}
}
