package com.carrotbay.domain.review;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carrotbay.common.handler.annotation.LogUpdateHistory;
import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.AuctionService;
import com.carrotbay.domain.review.dto.ReviewRequestDto;
import com.carrotbay.domain.review.dto.ReviewResponseDto;
import com.carrotbay.domain.review.dto.ReviewResponseDto.ListResponseDto;
import com.carrotbay.domain.review.exception.NotFoundReviewException;
import com.carrotbay.domain.review.repository.ReviewRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;
import com.carrotbay.domain.user.exception.UserMismatchException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ReviewService {

	private final ReviewRepository reviewRepository;
	private final UserService userService;
	private final AuctionService auctionService;

	@Transactional
	public ReviewResponseDto.PostResponseDto postReview(Long userId, ReviewRequestDto.CreateReviewDto postDto) {
		User user = userService.getUserById(userId);
		Auction auction = auctionService.getAuctionById(postDto.getAuctionId());
		auction.validateReviewable();
		Review review = reviewRepository.save(postDto.toEntity(user, auction));
		return new ReviewResponseDto.PostResponseDto(review.getId());
	}

	@LogUpdateHistory
	@Transactional
	public ReviewResponseDto.ModifyResponseDto modifyReview(Long userId, Long reviewId,
		ReviewRequestDto.ModifyReviewDto modifyDto) {
		Review review = checkReview(userId, reviewId);
		review.update(modifyDto.getTitle(), modifyDto.getContent());
		reviewRepository.save(review);
		return ReviewResponseDto.ModifyResponseDto.builder()
			.id(review.getId())
			.title(review.getTitle())
			.content(review.getContent())
			.createdAt(review.getCreatedAt())
			.auctionId(review.getAuction().getId())
			.userId(review.getUser().getId())
			.nickName(review.getUser().getNickname())
			.auctionTitle(review.getAuction().getTitle())
			.build();
	}

	@Transactional
	public ReviewResponseDto.DeleteResponseDto deleteReview(Long userId, Long reviewId) {
		Review review = checkReview(userId, reviewId);
		review.delete();
		reviewRepository.save(review);
		return new ReviewResponseDto.DeleteResponseDto(review.isDeleted());
	}

	@Transactional
	public Review checkReview(Long userId, Long reviewId) {
		User user = userService.getUserById(userId);
		Review review = reviewRepository.findById(reviewId).orElseThrow(
			() -> new NotFoundReviewException("해당하는 후기가 없습니다."));

		if (!user.getId().equals(review.getUser().getId())) {
			throw new UserMismatchException("작성자와 일치하지않습니다.");
		}
		return review;
	}

	@Transactional(readOnly = true)
	public List<ReviewResponseDto.ListResponseDto> findReviewList() {
		List<Review> reviewList = reviewRepository.findReviewList();
		return reviewList.stream()
			.map(review -> {
				return new ListResponseDto(
					review.getId(),
					review.getTitle(),
					review.getContent(),
					review.getCreatedAt(),
					review.getAuction().getId(),
					review.getUser().getId(),
					review.getUser().getNickname(),
					review.getAuction().getTitle());
			})
			.collect(Collectors.toList());

	}
}
