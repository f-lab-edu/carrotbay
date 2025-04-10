package com.carrotbay.domain.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.carrotbay.domain.review.Review;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewCustomRepository {

}
