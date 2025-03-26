package com.carrotbay.domain.auction;

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
import com.carrotbay.domain.auction.dto.AuctionDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/auctions")
@RequiredArgsConstructor
@RestController
public class AuctionController {

	private final AuctionService auctionService;

	@PostMapping("")
	public ResponseEntity<?> postAuction(@LoginUser Long userId, @RequestBody @Valid AuctionDto.CreateAuctionDto dto,
		BindingResult bindingResult) {
		AuctionDto.PostAuctionResponseDto result = auctionService.postAuction(userId, dto);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@PutMapping("/{auction_id}")
	public ResponseEntity<?> putAuction(@PathVariable(name = "auction_id") Long auctionId, @LoginUser Long userId,
		@RequestBody @Valid AuctionDto.ModifyAuctionDto dto, BindingResult bindingResult) {
		AuctionDto.ModifyAuctionResponseDto responseDto = auctionService.modifyAuction(userId, auctionId, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@DeleteMapping("/{auction_id}")
	public ResponseEntity<?> deleteAuction(@PathVariable(name = "auction_id") Long auctionId, @LoginUser long userId) {
		AuctionDto.DeleteAuctionResponseDto result = auctionService.deleteAuction(userId, auctionId);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@GetMapping("")
	public ResponseEntity<?> findAuctionList() {
		List<AuctionDto.AuctionResponseDto> result = auctionService.findAuctionList();
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
}
