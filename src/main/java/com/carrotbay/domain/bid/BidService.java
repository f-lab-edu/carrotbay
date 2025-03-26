package com.carrotbay.domain.bid;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.AuctionService;
import com.carrotbay.domain.bid.dto.BidDto;
import com.carrotbay.domain.bid.repository.BidRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class BidService {
	private final BidRepository bidRepository;
	private final UserService userService;
	private final AuctionService auctionService;

	@Transactional
	public Long postBid(Long userId, Long auctionId, BidDto.CreateBidDto postDto) {
		User user = userService.getUserById(userId);
		Auction auction = auctionService.getAuctionWithLock(auctionId);
		checkAuctionTime(auction.getEndDate());
		BidDto.BidResponseDto highestBid = bidRepository.findHighestBidByAuctionId(auctionId);
		if (highestBid != null && postDto.getBidPrice() <= highestBid.getBidPrice()) {
			throw new IllegalArgumentException("입찰가는 현재 최고 입찰가보다 높아야 합니다.");
		}
		Bid bid = bidRepository.save(postDto.toEntity(user, auction));
		return bid.getAuction().getId();
	}

	@Transactional
	public boolean cancelBid(Long userId, Long bidId, BidDto.BidCancelRequestDto dto) {
		User user = userService.getUserById(userId);
		Auction auction = auctionService.getAuctionById(dto.getAuctionId());
		checkAuctionTime(auction.getEndDate());
		Bid bid = bidRepository.findByIdAndCreatedBy(bidId, user).orElseThrow(
			() -> new IllegalArgumentException("입찰한 내역이 존재하지않습니다.")
		);
		bid.delete();
		bidRepository.save(bid);
		return true;
	}

	public List<BidDto.BidResponseDto> findBidListByAuction(Long auctionId) {
		return bidRepository.findBidListByAuctionId(auctionId);
	}

	public void checkAuctionTime(LocalDateTime endDate) {
		LocalDateTime nowTime = LocalDateTime.now();
		if (!endDate.isAfter(nowTime)) {
			throw new IllegalArgumentException("경매가 이미 종료되었습니다.");
		}
	}
}
