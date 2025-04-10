package com.carrotbay.domain.review;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrotbay.config.auth.LoginUser;
import com.carrotbay.domain.review.dto.ReviewRequestDto;
import com.carrotbay.domain.review.dto.ReviewResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@RestController
public class ReviewController {
	private final ReviewService reviewService;

	@PostMapping("")
	public ResponseEntity<?> postAuction(@LoginUser Long userId,
		@RequestBody @Valid ReviewRequestDto.CreateReviewDto dto,
		BindingResult bindingResult) {
		ReviewResponseDto.PostResponseDto result = reviewService.postReview(userId, dto);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@PutMapping("/{review_id}")
	public ResponseEntity<?> putAuction(@PathVariable(name = "review_id") Long reviewId, @LoginUser Long userId,
		@RequestBody @Valid ReviewRequestDto.ModifyReviewDto dto, BindingResult bindingResult) {
		ReviewResponseDto.ModifyResponseDto responseDto = reviewService.modifyReview(userId, reviewId, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@DeleteMapping("/{review_id}")
	public ResponseEntity<?> deleteAuction(@PathVariable(name = "review_id") Long reviewId, @LoginUser long userId) {
		ReviewResponseDto.DeleteResponseDto result = reviewService.deleteReview(userId, reviewId);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@GetMapping("")
	public ResponseEntity<?> findAuctionList() {
		List<ReviewResponseDto.ListResponseDto> result = reviewService.findReviewList();
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
}
