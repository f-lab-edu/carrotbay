package com.carrotbay.domain.bid;

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
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.bid.dto.BidRequestDto;
import com.carrotbay.domain.bid.exception.NotFoundBidException;
import com.carrotbay.domain.bid.repository.BidRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;
import com.carrotbay.dummy.DummyObject;

@ExtendWith(MockitoExtension.class)
class BidServiceTest extends DummyObject {

	@InjectMocks
	private BidService bidService;
	@Mock
	private AuctionService auctionService;
	@Mock
	private UserService userService;
	@Mock
	private AuctionRepository auctionRepository;
	@Mock
	private BidRepository bidRepository;
	private final Long userId = 999L;
	private final Long auctionId = 999L;
	private final Long bidId = 999L;

	@Test
	@DisplayName("경매 시간이 현재 시간보다 늦은 경우 입찰에 실패한다.")
	void 입찰등록_실패케이스_경매시각이_지난_경우_입찰에_실패한다() {
		// given
		User user = newMockUser(userId, "test");
		Auction auction = newMockExpiredAuction(auctionId, user);
		BidRequestDto.CreateBidDto dto = new BidRequestDto.CreateBidDto(1000, auction.getId());
		when(userService.getUserById(any())).thenReturn(user);
		when(auctionService.getAuctionWithLock(any())).thenReturn(auction);
		// when & then
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> bidService.postBid(user.getId(), dto));
		assertEquals("경매가 이미 종료되었습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("입찰가가 최고 입찰가보다 작은 경우 입찰에 실패한다.")
	void 입찰등록_실패케이스_최고입찰가보다_입찰가가_작은_경우_입찰에_실패한다() {
		// given
		User user = newMockUser(userId, "test");
		Auction auction = newMockAuction(auctionId, user);
		BidRequestDto.CreateBidDto dto = new BidRequestDto.CreateBidDto(10, auction.getId());
		Bid responseDto = newBid(bidId, user, auction);

		when(userService.getUserById(any())).thenReturn(user);
		when(auctionService.getAuctionWithLock(any())).thenReturn(auction);
		when(bidRepository.findHighestBidByAuctionId(auction.getId())).thenReturn(responseDto);
		// when
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> bidService.postBid(user.getId(), dto));
		// then
		assertEquals("입찰가는 현재 최고 입찰가보다 높아야 합니다.", exception.getMessage());
	}

	@Test
	@DisplayName("입찰등록 성공케이스")
	void 입찰등록_성공케이스() {
		// given
		User user = newMockUser(userId, "test");
		Auction auction = newMockAuction(auctionId, user);
		BidRequestDto.CreateBidDto dto = new BidRequestDto.CreateBidDto(10000, auction.getId());
		Bid bid = newBid(bidId, user, auction);
		Bid responseDto = newBid(bidId, user, auction);

		when(userService.getUserById(any())).thenReturn(user);
		when(auctionService.getAuctionWithLock(any())).thenReturn(auction);
		when(bidRepository.findHighestBidByAuctionId(auction.getId())).thenReturn(responseDto);
		when(bidRepository.save(any())).thenReturn(bid);
		// when
		Long returnAuctionId = bidService.postBid(user.getId(), dto);
		// then
		Assertions.assertThat(returnAuctionId).isEqualTo(bid.getAuction().getId());
	}

	@Test
	@DisplayName("입찰가가 최고 입찰가보다 작은 경우 입찰에 실패한다.")
	void 입찰수정_실패케이스_입찰이_존재하지않는_경우_입찰에_실패한다() {
		// given
		BidRequestDto.DeleteBidDto dto = new BidRequestDto.DeleteBidDto(auctionId);
		User user = newMockUser(userId, "test");
		Auction auction = newMockAuction(auctionId, user);

		when(userService.getUserById(any())).thenReturn(user);
		when(auctionService.getAuctionById(any())).thenReturn(auction);
		when(bidRepository.findByIdAndCreatedBy(bidId, user)).thenReturn(Optional.empty());
		// when
		NotFoundBidException exception = assertThrows(NotFoundBidException.class,
			() -> bidService.cancelBid(user.getId(), bidId, dto));
		// then
		assertEquals("입찰한 내역이 존재하지않습니다.", exception.getMessage());
	}

	@Test
	@DisplayName("입찰가가 최고 입찰가보다 작은 경우 입찰에 성공한다.")
	void 입찰수정_성공케이스() {
		// given
		BidRequestDto.DeleteBidDto dto = new BidRequestDto.DeleteBidDto(auctionId);
		User user = newMockUser(userId, "test");
		Auction auction = newMockAuction(auctionId, user);
		Bid bid = newBid(bidId, user, auction);

		when(userService.getUserById(any())).thenReturn(user);
		when(auctionService.getAuctionById(any())).thenReturn(auction);
		when(bidRepository.findByIdAndCreatedBy(bidId, user)).thenReturn(Optional.ofNullable(bid));
		when(bidRepository.save(any())).thenReturn(bid);
		// when
		boolean result = bidService.cancelBid(user.getId(), bidId, dto);
		// then
		assertEquals(result, true);
	}
}