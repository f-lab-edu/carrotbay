package com.carrotbay.domain.auction;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.carrotbay.domain.auction.dto.AuctionDto;
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final UserService userService;

	public AuctionDto.PostAuctionResponseDto postAuction(Long userId, AuctionDto.CreateAuctionDto postDto) {
		User user = userService.getUserById(userId);
		Auction auction = auctionRepository.save(postDto.toEntity(user));
		return AuctionDto.PostAuctionResponseDto.builder()
			.id(auction.getId())
			.build();
	}

	public AuctionDto.ModifyAuctionResponseDto modifyAuction(Long userId, Long auctionId,
		AuctionDto.ModifyAuctionDto modifyDto) {

		User user = userService.getUserById(userId);
		Auction auction = validateAuctionOwner(user.getId(), auctionId);
		auction.update(modifyDto.getTitle(), modifyDto.getContent(), modifyDto.getEndDate(),
			modifyDto.getMinimumPrice(), modifyDto.getInstantPrice());
		Auction savedAuction = auctionRepository.save(auction);

		return AuctionDto.ModifyAuctionResponseDto.builder()
			.id(savedAuction.getId())
			.title(savedAuction.getTitle())
			.content(savedAuction.getContent())
			.status(savedAuction.getStatus().getStatus())
			.startDate(savedAuction.getCreatedAt())
			.endDate(savedAuction.getEndDate())
			.minimumPrice(savedAuction.getMinimumPrice())
			.instantPrice(savedAuction.getInstantPrice())
			.successfulBidderId(
				auction.getSuccessfulBidder() != null ? auction.getSuccessfulBidder().getId() : null)
			.createdBy(savedAuction.getCreatedBy().getId())
			.creator(savedAuction.getCreatedBy().getNickname())
			.build();
	}

	public AuctionDto.DeleteAuctionResponseDto deleteAuction(Long userId, Long auctionId) {
		User user = userService.getUserById(userId);
		Auction auction = validateAuctionOwner(user.getId(), auctionId);
		auction.delete();
		auctionRepository.save(auction);
		return AuctionDto.DeleteAuctionResponseDto.builder()
			.isDeleted(true)
			.build();
	}

	public List<AuctionDto.AuctionResponseDto> findAuctionList() {
		return auctionRepository.findAuctionList();
	}

	public Auction validateAuctionOwner(Long userId, Long auctionId) {
		Auction auction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new IllegalArgumentException("해당 경매내역이 존재하지않습니다."));
		if (!Objects.equals(userId, auction.getCreatedBy().getId())) {
			throw new IllegalArgumentException("작성자가 아닙니다.");
		}
		return auction;
	}

	public Auction getAuctionWithLock(Long id) {
		return auctionRepository.getAuctionWithLock(id).orElseThrow(
			() -> new IllegalArgumentException("해당 경매가 존재하지않습니다.")
		);
	}

	public Auction getAuctionById(Long id) {
		return auctionRepository.findById(id).orElseThrow(
			() -> new IllegalArgumentException("해당 경매가 존재하지않습니다.")
		);
	}
}
