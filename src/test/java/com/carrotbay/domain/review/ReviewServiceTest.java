package com.carrotbay.domain.review;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.AuctionService;
import com.carrotbay.domain.review.dto.ReviewRequestDto;
import com.carrotbay.domain.review.dto.ReviewResponseDto;
import com.carrotbay.domain.review.repository.ReviewRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;
import com.carrotbay.dummy.DummyObject;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest extends DummyObject {

	@InjectMocks
	private ReviewService reviewService;
	@Mock
	private AuctionService auctionService;
	@Mock
	private UserService userService;
	@Mock
	private ReviewRepository reviewRepository;
	private final Long userId = 999L;
	private final Long auctionId = 999L;
	private final Long reviewId = 999L;

	@Test
	@DisplayName("경매가 완료되어야 후기를 작성 할 수 있다.")
	void 후기등록_실패케이스_경매완료_상태여야_후기_등록_가능() {
		// given

		User user = newMockUser(userId, "test");
		Auction auction = newMockAuction(auctionId, user);
		ReviewRequestDto.CreateReviewDto dto = new ReviewRequestDto.CreateReviewDto("title", "content",
			auction.getId());

		when(userService.getUserById(any())).thenReturn(user);
		when(auctionService.getAuctionById(any())).thenReturn(auction);
		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> reviewService.postReview(user.getId(), dto));

		assertEquals("완료된 경매에만 후기를 등록 할 수 있습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("후기 등록 성공")
	void 후기등록_성공케이스() {
		// given
		User user = newMockUser(userId, "test");
		Auction auction = newMockExpiredAuction(auctionId, user);
		ReviewRequestDto.CreateReviewDto dto = new ReviewRequestDto.CreateReviewDto("title", "content",
			auction.getId());
		Review review = newReview(reviewId, user, auction);
		when(userService.getUserById(any())).thenReturn(user);
		when(auctionService.getAuctionById(any())).thenReturn(auction);
		when(reviewRepository.save(any())).thenReturn(review);

		// when
		ReviewResponseDto.PostResponseDto result = reviewService.postReview(user.getId(), dto);

		// then
		Assertions.assertThat(result.getId()).isEqualTo(review.getId());
	}

	@Test
	@DisplayName("후기 수정시 후기가 존재않으면 실패")
	void 후기수정_실패케이스_후기가_존재하지않으면_실패() {
		// given
		ReviewRequestDto.ModifyReviewDto dto = new ReviewRequestDto.ModifyReviewDto("title", "content");
		User user = newMockUser(userId, "test");
		when(userService.getUserById(any())).thenReturn(user);

		// when & then
		NullPointerException exception = assertThrows(NullPointerException.class,
			() -> reviewService.modifyReview(user.getId(), reviewId, dto));
		assertEquals("해당하는 후기가 없습니다.", exception.getMessage());

	}

	@Test
	@DisplayName("후기 수작성자와 로그인한 사용자가 일치않으면 실패한다.")
	void 후기수정_실패케이스_후기작성자와_로그인한_사용자가_일치하지않으면_실패() {
		// given
		ReviewRequestDto.ModifyReviewDto dto = new ReviewRequestDto.ModifyReviewDto("title", "content");
		User loginUser = newMockUser(userId, "test");
		User reviewUser = newMockUser(2L, "test");
		Auction auction = newMockExpiredAuction(auctionId, reviewUser);
		Review review = newReview(reviewId, reviewUser, auction);
		when(userService.getUserById(any())).thenReturn(loginUser);
		when(reviewRepository.findById(any())).thenReturn(Optional.of(review));

		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> reviewService.modifyReview(loginUser.getId(), auction.getId(), dto));
		assertEquals("작성자와 일치하지않습니다.", exception.getMessage());

	}

	@Test
	@DisplayName("후기 수정 성공 케이스.")
	void 후기수정_성공케이스() {
		// given
		String title = "title update";
		String content = "content update";

		ReviewRequestDto.ModifyReviewDto request = new ReviewRequestDto.ModifyReviewDto(title, content);
		User user = newMockUser(userId, "test");
		Auction auction = newMockExpiredAuction(auctionId, user);
		Review review = newReview(reviewId, user, auction);
		when(userService.getUserById(any())).thenReturn(user);
		when(reviewRepository.findById(any())).thenReturn(Optional.of(review));
		when(reviewRepository.save(any())).thenReturn(review);

		// when
		ReviewResponseDto.ModifyResponseDto result = reviewService.modifyReview(user.getId(), reviewId, request);

		// then
		Assertions.assertThat(result.getId()).isEqualTo(review.getId());
		Assertions.assertThat(result.getTitle()).isEqualTo(request.getTitle());
		Assertions.assertThat(result.getContent()).isEqualTo(request.getContent());
		Assertions.assertThat(result.getAuctionId()).isEqualTo(auction.getId());
		Assertions.assertThat(result.getUserId()).isEqualTo(user.getId());
		Assertions.assertThat(result.getNickName()).isEqualTo(user.getNickname());
		Assertions.assertThat(result.getAuctionTitle()).isEqualTo(auction.getTitle());
	}

	@Test
	@DisplayName("후기 삭제 성공 케이스.")
	void 후기삭제_성공케이스() {
		User user = newMockUser(userId, "test");
		Auction auction = newMockExpiredAuction(auctionId, user);
		Review review = newReview(reviewId, user, auction);
		when(userService.getUserById(any())).thenReturn(user);
		when(reviewRepository.findById(any())).thenReturn(Optional.of(review));
		when(reviewRepository.save(any())).thenReturn(review);
		// when
		ReviewResponseDto.DeleteResponseDto result = reviewService.deleteReview(user.getId(), reviewId);

		// then
		Assertions.assertThat(result.isDeleted()).isEqualTo(true);
	}
}
