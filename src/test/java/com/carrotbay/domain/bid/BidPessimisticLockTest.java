package com.carrotbay.domain.bid;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.bid.dto.BidDto;
import com.carrotbay.domain.bid.repository.BidRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.repository.UserRepository;
import com.carrotbay.dummy.DummyObject;

@SpringBootTest
class BidPessimisticLockTest extends DummyObject {
	@Autowired
	private BidRepository bidRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	@Autowired
	private BidService bidService;

	@Autowired
	private UserRepository userRepository;

	private Auction auction;
	private User user;

	@BeforeEach
	void setup() {
		User testUser = newUser("auction");
		user = userRepository.save(testUser);
		Auction testAuction = newAuction(user);
		auction = auctionRepository.save(testAuction);
	}

	@AfterEach
	void cleanup() {
		bidRepository.deleteAll();
		auctionRepository.delete(auction);
		userRepository.delete(user);
	}

	@Test
	@DisplayName("동시에 여러 개의 입찰이 들어올 때 가장 최고가 외에는 입찰에 실패해야한다.")
	void testSimultaneousBidsWithPessimisticLock() throws InterruptedException {
		// given
		List<Integer> bidPrices = List.of(120, 150, 180, 110, 170, 140, 160, 190, 130, 200, 400);
		CountDownLatch countDownLatch = new CountDownLatch(bidPrices.size());
		ExecutorService executor = Executors.newFixedThreadPool(bidPrices.size());
		for (int i = 0; i < bidPrices.size(); i++) {
			final Integer bidPrice = bidPrices.get(i);
			executor.submit(() -> {
				try {
					bidService.postBid(user.getId(), auction.getId(),
						new BidDto.CreateBidDto(bidPrice));
				} catch (Exception e) {
					System.out.println("입찰 실패: " + bidPrice + " " + e.getMessage());
				} finally {
					countDownLatch.countDown();
				}
			});
		}

		countDownLatch.await();
		// then
		BidDto.BidResponseDto highestBid = bidRepository.findHighestBidByAuctionId(auction.getId());
		assertThat(highestBid).isNotNull();
		assertThat(highestBid.getBidPrice()).isEqualTo(400);
		assertThat(highestBid.getUserId()).isEqualTo(user.getId());
	}
}