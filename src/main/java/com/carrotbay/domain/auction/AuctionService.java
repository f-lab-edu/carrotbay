package com.carrotbay.domain.auction;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.carrotbay.domain.auction.dto.AuctionDto;
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuctionService {

	private final AuctionRepository auctionRepository;
	private final UserService userService;

	public Long postAuction(HttpSession session, AuctionDto.UpsertAuctionDto postDto) {
		User findUser = userService.getUserById(session);
		Auction auction = auctionRepository.save(postDto.toEntity(findUser));
		return auction.getId();
	}

	public AuctionDto.AuctionResponseDto modifyAuction(HttpSession session, Long auctionId,
		AuctionDto.UpsertAuctionDto modifyDto) {
		User findUser = userService.getUserById(session);
		Auction findAuction = auctionRepository.findById(auctionId)
			.orElseThrow(() -> new IllegalArgumentException("해당 경매내역이 존재하지않습니다."));

		if (!Objects.equals(findUser.getId(), findAuction.getCreatedBy().getId())) {
			throw new IllegalArgumentException("작성자가 아닙니다.");
		}
		findAuction.update(modifyDto);
		Auction saveAuction = auctionRepository.save(findAuction);
		return new AuctionDto.AuctionResponseDto(saveAuction);
	}

	public boolean deleteAuction(HttpSession session, Long id) {
		User findUser = userService.getUserById(session);
		Auction findAuction = auctionRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 경매내역이 존재하지않습니다."));

		if (!Objects.equals(findUser.getId(), findAuction.getCreatedBy().getId())) {
			throw new IllegalArgumentException("작성자가 아닙니다.");
		}
		findAuction.update(true);
		auctionRepository.save(findAuction);
		return true;
	}

	public List<AuctionDto.AuctionResponseDto> getAuctionList(AuctionDto.AuctionRequestDto dto) {
		return auctionRepository.getAuctionList(dto);
	}
}
