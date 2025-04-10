package com.carrotbay.domain.review.repository;

import static com.carrotbay.domain.review.QReview.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.carrotbay.domain.review.Review;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ReviewCustomRepositoryImpl implements ReviewCustomRepository {
	private final JPAQueryFactory query;

	@Override
	public Review findReview(Long reviewId) {
		return query.select(review)
			.from(review)
			.where(review.id.eq(reviewId))
			.leftJoin(review.user).fetchJoin()
			.leftJoin(review.auction).fetchJoin()
			.fetchFirst();
	}

	@Override
	public List<Review> findReviewList() {
		return query.select(review)
			.from(review)
			.leftJoin(review.user).fetchJoin()
			.leftJoin(review.auction).fetchJoin()
			.fetch();
	}

}
