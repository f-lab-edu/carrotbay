package com.carrotbay.dummy;

import java.time.LocalDateTime;

import org.mindrot.jbcrypt.BCrypt;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.AuctionStatus;
import com.carrotbay.domain.bid.Bid;
import com.carrotbay.domain.bid.BidStatus;
import com.carrotbay.domain.review.Review;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserStatus;

public class DummyObject {

	protected User newUser(String nickname) {
		String hashed = BCrypt.hashpw("test1234T@", BCrypt.gensalt());
		return User.builder()
			.username(nickname + "@naver.com")
			.password(hashed)
			.nickname(nickname)
			.status(UserStatus.ACTIVE)
			.introduce("자기소개입니다.")
			.imageUrl("http://image.com")
			.build();
	}

	protected User newMockUser(Long id, String nickname) {
		String hashed = BCrypt.hashpw("test1234T@", BCrypt.gensalt());
		return User.builder()
			.id(id)
			.username(nickname + "@naver.com")
			.password(hashed)
			.nickname(nickname)
			.status(UserStatus.ACTIVE)
			.introduce("자기소개입니다.")
			.imageUrl("http://image.com")
			.build();
	}

	protected Auction newMockAuction(Long id, User user) {
		return Auction.builder()
			.id(id)
			.title("test")
			.content("test content")
			.status(AuctionStatus.AUCTION)
			.minimumPrice(10)
			.instantPrice(10)
			.isDelete(false)
			.createdAt(LocalDateTime.now())
			.endDate(LocalDateTime.now().plusMonths(1))
			.createdBy(user.getId())
			.user(user)
			.build();
	}

	protected Auction newMockExpiredAuction(Long id, User user) {
		return Auction.builder()
			.id(id)
			.title("test")
			.content("test content")
			.status(AuctionStatus.CLOSE)
			.minimumPrice(10)
			.instantPrice(10)
			.isDelete(false)
			.createdAt(LocalDateTime.now().minusDays(1))
			.endDate(LocalDateTime.now().minusDays(1))
			.createdBy(user.getId())
			.user(user)
			.build();
	}

	protected Auction newAuction(User user) {
		return Auction.builder()
			.title("test")
			.content("test content")
			.status(AuctionStatus.AUCTION)
			.endDate(LocalDateTime.now().plusMonths(1))
			.minimumPrice(10)
			.instantPrice(10)
			.isDelete(false)
			.createdAt(LocalDateTime.now())
			.user(user)
			.createdBy(user.getId())
			.build();
	}

	protected Bid newBid(Long id, User user, Auction auction) {
		return Bid.builder()
			.id(id)
			.auction(auction)
			.bidPrice(1000)
			.createdAt(LocalDateTime.now())
			.status(BidStatus.BID)
			.user(user)
			.createdBy(user.getId())
			.build();
	}

	protected Review newReview(Long id, User user, Auction auction) {
		return Review.builder()
			.id(id)
			.title("test title")
			.content("test content")
			.user(user)
			.auction(auction)
			.createdBy(user.getId())
			.build();
	}
}
