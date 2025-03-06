package com.carrotbay.domain.auction;

import java.time.LocalDateTime;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.carrotbay.domain.auction.dto.AuctionDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequestMapping("/api/auctions")
@RequiredArgsConstructor
@RestController
public class AuctionController {

	private final AuctionService auctionService;

	@PostMapping("")
	public ResponseEntity<?> postAuction(@RequestBody @Valid AuctionDto.UpsertAuctionDto dto,
		BindingResult bindingResult, HttpServletRequest httpServletRequest) {
		HttpSession httpSession = httpServletRequest.getSession(true);
		Long id = auctionService.postAuction(httpSession, dto);
		return new ResponseEntity<>(id, HttpStatus.CREATED);
	}

	@PutMapping("/{auction_id}")
	public ResponseEntity<?> putAuction(@PathVariable(name = "auction_id") Long auctionId,
		@RequestBody @Valid AuctionDto.UpsertAuctionDto dto,
		BindingResult bindingResult, HttpServletRequest httpServletRequest) {
		HttpSession httpSession = httpServletRequest.getSession(true);
		AuctionDto.AuctionResponseDto responseDto = auctionService.modifyAuction(httpSession, auctionId, dto);
		return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
	}

	@DeleteMapping("/{auction_id}")
	public ResponseEntity<?> deleteAuction(@PathVariable(name = "auction_id") Long auctionId,
		@RequestBody @Valid AuctionDto.UpsertAuctionDto dto,
		BindingResult bindingResult, HttpServletRequest httpServletRequest) {
		HttpSession httpSession = httpServletRequest.getSession(true);
		boolean result = auctionService.deleteAuction(httpSession, auctionId);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}

	@GetMapping("")
	public ResponseEntity<?> getAuctionList(@RequestParam(name = "auction_id", required = false) Long auctionId,
		@RequestParam(name = "title", required = false) String title,
		@RequestParam(name = "start_date", required = false) LocalDateTime startDate,
		@RequestParam(name = "created_by", required = false) LocalDateTime createdBy,
		@RequestParam(name = "page_number", required = false, defaultValue = "1") int pageNumber,
		@RequestParam(name = "page_size", required = false, defaultValue = "50") int pageSize) {
		AuctionDto.AuctionRequestDto responseDto = new AuctionDto.AuctionRequestDto(auctionId, title, startDate,
			createdBy, pageNumber, pageSize);
		List<AuctionDto.AuctionResponseDto> result = auctionService.getAuctionList(responseDto);
		return new ResponseEntity<>(result, HttpStatus.CREATED);
	}
}
