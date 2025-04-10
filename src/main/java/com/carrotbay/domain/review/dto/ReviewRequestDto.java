package com.carrotbay.domain.review.dto;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.review.Review;
import com.carrotbay.domain.user.User;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewRequestDto {

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CreateReviewDto {

		@NotEmpty(message = "제목을 입력해주세요.")
		@Size(min = 5, max = 20, message = "제목은 5자 이상 20자 이하로 입력해주세요.")
		private String title;

		@NotEmpty(message = "본문을 입력해주세요.")
		private String content;

		@NotNull(message = "경매 id를 입력해주세요.")
		private Long auctionId;

		public Review toEntity(User user, Auction auction) {
			return Review.builder()
				.title(this.title)
				.content(this.content)
				.user(user)
				.auction(auction)
				.createdBy(user.getId())
				.build();
		}
	}

	@Getter
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ModifyReviewDto {

		@NotEmpty(message = "제목을 입력해주세요.")
		@Size(min = 5, max = 20, message = "제목은 5자 이상 20자 이하로 입력해주세요.")
		private String title;

		@NotEmpty(message = "본문을 입력해주세요.")
		private String content;
	}
}
