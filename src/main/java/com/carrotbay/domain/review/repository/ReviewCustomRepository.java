package com.carrotbay.domain.review.repository;

import java.util.List;

import com.carrotbay.domain.review.Review;

public interface ReviewCustomRepository {

	public Review findReview(Long reviewId);

	public List<Review> findReviewList();
}
