package com.carrotbay.domain.auction;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.carrotbay.domain.auction.dto.AuctionRequestDto;
import com.carrotbay.domain.auction.dto.AuctionResponseDto;
import com.carrotbay.domain.auction.exception.NotFoundAuctionException;
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;
import com.carrotbay.domain.user.exception.NotFoundUserException;
import com.carrotbay.dummy.DummyObject;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest extends DummyObject {

	@InjectMocks
	private AuctionService auctionService;

	@Mock
	private UserService userService;

	@Mock
	private AuctionRepository auctionRepository;

	private final Long fakeSessionId = 999L;
	private final Long fakeAuctionId = 999L;

	@Test
	@DisplayName("사용자가 존재하지않으면 등록에 실패한다.")
	void 경매등록_실패케이스_사용자가_존재하지않는_경우_등록에_실패한다() {
		// given
		AuctionRequestDto.CreateAuctionDto dto = new AuctionRequestDto.CreateAuctionDto();
		given(userService.getUserById(fakeSessionId)).willThrow(new NotFoundUserException("해당 사용자가 존재하지않습니다."));

		// when & then
		assertThrows(NotFoundUserException.class, () -> auctionService.postAuction(fakeSessionId, dto));
	}

	@Test
	@DisplayName("경매등록_성공케이스")
	void 경매등록_성공케이스() {
		// given
		AuctionRequestDto.CreateAuctionDto dto = new AuctionRequestDto.CreateAuctionDto();
		User user = newMockUser(fakeSessionId, "test");
		Auction auction = newMockAuction(fakeAuctionId, user);
		when(userService.getUserById(any())).thenReturn(user);
		when(auctionRepository.save(any())).thenReturn(auction);
		// when
		AuctionResponseDto.PostResponseDto responseDto = auctionService.postAuction(fakeSessionId, dto);
		// then
		Assertions.assertThat(responseDto.getId()).isEqualTo(fakeAuctionId);
	}

	@Test
	@DisplayName("session이 null이면 경매 수정에 실패한다.")
	void 경매수정_실패케이스_Session이_null인_경우_수정에_실패한다() {
		// given
		String exceptionMessage = "session이 null입니다";
		AuctionRequestDto.ModifyAuctionDto dto = new AuctionRequestDto.ModifyAuctionDto();
		// when
		given(userService.getUserById(null)).willThrow(new NotFoundAuctionException(exceptionMessage));
		NotFoundAuctionException exception = assertThrows(NotFoundAuctionException.class,
			() -> auctionService.modifyAuction(null, fakeAuctionId, dto));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("aucionID가 존재하지않으면 경매 수정에 실패한다.")
	void 경매수정_실패케이스_actionId가_null인_경우_수정에_실패한다() {
		// given
		String exceptionMessage = "해당 경매내역이 존재하지않습니다.";
		AuctionRequestDto.ModifyAuctionDto dto = new AuctionRequestDto.ModifyAuctionDto();
		User user = newMockUser(fakeSessionId, "test");
		// when
		lenient().when(userService.getUserById(any())).thenReturn(user);
		given(auctionRepository.findById(fakeAuctionId)).willThrow(new IllegalArgumentException(exceptionMessage));
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> auctionService.modifyAuction(fakeSessionId, fakeAuctionId, dto));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("auctionId와 작성자의 id가 틀린 경우 수정에 실패한다.")
	void 경매수정_실패케이스_actionId와_작성자의_id가_틀린_경우_수정에_실패한다() {
		// given
		String exceptionMessage = "작성자가 아닙니다.";
		AuctionRequestDto.ModifyAuctionDto dto = new AuctionRequestDto.ModifyAuctionDto();
		User user = newMockUser(fakeSessionId, "test");
		User writerUser = newMockUser(1L, "writerUser");
		Auction auction = newMockAuction(fakeAuctionId, writerUser);

		// when
		lenient().when(userService.getUserById(any())).thenReturn(user);
		lenient().when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> auctionService.modifyAuction(null, fakeAuctionId, dto));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("경매수정 성공케이스.")
	void 경매수정_성공케이스() {
		// given
		String title = "test";
		String content = "content";
		LocalDateTime endDate = LocalDateTime.now();
		int minimumPrice = 10;
		int instantPrice = 10;
		AuctionRequestDto.ModifyAuctionDto dto = AuctionRequestDto.ModifyAuctionDto.builder()
			.title(title)
			.content(content)
			.endDate(endDate)
			.minimumPrice(minimumPrice)
			.instantPrice(instantPrice)
			.build();
		User user = newMockUser(fakeSessionId, title);
		Auction auction = newMockAuction(fakeAuctionId, user);
		lenient().when(userService.getUserById(any())).thenReturn(user);
		lenient().when(auctionRepository.findById(any())).thenReturn(Optional.of(auction));
		lenient().when(auctionRepository.save(any())).thenReturn(auction);
		// // when
		AuctionResponseDto.ModifyResponseDto responseDto = auctionService.modifyAuction(fakeSessionId, fakeAuctionId,
			dto);
		// then
		Assertions.assertThat(auction.getId()).isEqualTo(fakeAuctionId);
		Assertions.assertThat(responseDto.getTitle()).isEqualTo(title);
		Assertions.assertThat(responseDto.getContent()).isEqualTo(content);
		Assertions.assertThat(responseDto.getEndDate()).isEqualTo(endDate);
		Assertions.assertThat(responseDto.getMinimumPrice()).isEqualTo(minimumPrice);
		Assertions.assertThat(responseDto.getInstantPrice()).isEqualTo(instantPrice);
	}

	@Test
	@DisplayName("session이 null이면 경매 삭제에 실패한다.")
	void 경매수정_실패케이스_Session이_null인_경우_삭제에_실패한다() {
		// given
		String exceptionMessage = "session이 null입니다";
		// when
		given(userService.getUserById(null)).willThrow(new NotFoundAuctionException(exceptionMessage));
		NotFoundAuctionException exception = assertThrows(NotFoundAuctionException.class,
			() -> auctionService.deleteAuction(null, fakeAuctionId));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("aucionID가 존재하지않으면 경매 삭제에 실패한다.")
	void 경매삭제_실패케이스_actionId가_null인_경우_삭제에_실패한다() {
		// given
		String exceptionMessage = "해당 경매내역이 존재하지않습니다.";
		User user = newMockUser(fakeSessionId, "test");
		// when
		lenient().when(userService.getUserById(any())).thenReturn(user);
		given(auctionRepository.findById(fakeAuctionId)).willThrow(new IllegalArgumentException(exceptionMessage));
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> auctionService.deleteAuction(fakeSessionId, fakeAuctionId));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("auctionId와 작성자의 id가 틀린 경우 삭제에 실패한다.")
	void 경매삭제_실패케이스_actionId와_작성자의_id가_틀린_경우_삭제에_실패한다() {
		// given
		String exceptionMessage = "작성자가 아닙니다.";
		User user = newMockUser(fakeSessionId, "test");
		User writerUser = newMockUser(1L, "writerUser");
		Auction auction = newMockAuction(fakeAuctionId, writerUser);

		// when
		lenient().when(userService.getUserById(any())).thenReturn(user);
		lenient().when(auctionRepository.findById(any())).thenReturn(Optional.ofNullable(auction));
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> auctionService.deleteAuction(null, fakeAuctionId));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("경매조회_성공케이스.")
	void 경매조회_성공케이스() {
		// Given

		User createdBy = newMockUser(1L, "test");
		User createdBy2 = newMockUser(2L, "test2");
		List<AuctionResponseDto.AuctionListResponseDto> mockResponse = List.of(
			AuctionResponseDto.AuctionListResponseDto.builder()
				.id(1L)
				.title("Auction 1")
				.content("Description 1")
				.status(AuctionStatus.AUCTION)
				.endDate(LocalDateTime.now())
				.startDate(LocalDateTime.now())
				.createdBy(1L)
				.creator(createdBy.getNickname())
				.build(),
			AuctionResponseDto.AuctionListResponseDto.builder()
				.id(2L)
				.title("Auction 2")
				.content("Description 2")
				.status(AuctionStatus.CANCEL)
				.endDate(LocalDateTime.now())
				.startDate(LocalDateTime.now())
				.createdBy(1L)
				.creator(createdBy.getNickname())
				.build()
		);

		when(auctionRepository.findAuctionList()).thenReturn(mockResponse);

		// When (테스트 실행)
		List<AuctionResponseDto.AuctionListResponseDto> result = auctionService.findAuctionList();

		// Then (검증)
		assertNotNull(result);
		assertEquals(2, result.size());

		assertEquals(1L, result.get(0).getId());
		assertEquals("Auction 1", result.get(0).getTitle());
		assertEquals("Description 1", result.get(0).getContent());
		assertEquals(AuctionStatus.AUCTION.getStatus(), result.get(0).getStatus());
		assertEquals(1L, result.get(0).getCreatedBy());
		assertEquals("test", result.get(0).getCreator());

		assertEquals(2L, result.get(1).getId());
		assertEquals("Auction 2", result.get(1).getTitle());
		assertEquals("Description 2", result.get(1).getContent());
		assertEquals(AuctionStatus.CANCEL.getStatus(), result.get(1).getStatus());
		assertEquals(1L, result.get(1).getCreatedBy());
		assertEquals("test", result.get(0).getCreator());

		verify(auctionRepository, times(1)).findAuctionList();
	}
}