package com.carrotbay.domain.bid;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.carrotbay.config.auth.LoginUser;
import com.carrotbay.domain.bid.dto.BidRequestDto;
import com.carrotbay.domain.bid.dto.BidResponseDto;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/bids")
@RequiredArgsConstructor
@RestController
public class BidController {

	private final BidService bidService;

	@PostMapping("/{auction_id}")
	public ResponseEntity<?> postBid(@PathVariable(name = "auction_id") Long auctionId, @LoginUser Long userId,
		@RequestBody @Valid BidRequestDto.CreateBidDto dto, BindingResult bindingResult) {
		Long result = bidService.postBid(userId, auctionId, dto);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@PutMapping("/{bid_id}")
	public ResponseEntity<?> cancelBid(@PathVariable(name = "bid_id") Long bidId, @LoginUser Long userId,
		@RequestBody @Valid BidRequestDto.DeleteBidDto dto, BindingResult bindingResult) {
		boolean result = bidService.cancelBid(userId, bidId, dto);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@GetMapping("/{auction_id}")
	public ResponseEntity<?> getBidListByAuction(@PathVariable(name = "auction_id") Long auctionId) {
		List<BidResponseDto.BidListResponseDto> result = bidService.findBidListByAuction(auctionId);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
}
