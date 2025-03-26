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

import com.carrotbay.domain.auction.dto.AuctionDto;
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;
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
		AuctionDto.CreateAuctionDto dto = new AuctionDto.CreateAuctionDto();
		given(userService.getUserById(fakeSessionId)).willThrow(new NullPointerException("해당 사용자가 존재하지않습니다."));

		// when & then
		assertThrows(NullPointerException.class, () -> auctionService.postAuction(fakeSessionId, dto));
	}

	@Test
	@DisplayName("경매등록_성공케이스")
	void 경매등록_성공케이스() {
		// given
		AuctionDto.CreateAuctionDto dto = new AuctionDto.CreateAuctionDto();
		User user = newMockUser(fakeSessionId, "test");
		Auction auction = newMockAuction(fakeAuctionId, user);
		when(userService.getUserById(any())).thenReturn(user);
		when(auctionRepository.save(any())).thenReturn(auction);
		// when
		AuctionDto.PostAuctionResponseDto responseDto = auctionService.postAuction(fakeSessionId, dto);
		// then
		Assertions.assertThat(responseDto.getId()).isEqualTo(fakeAuctionId);
	}

	@Test
	@DisplayName("session이 null이면 경매 수정에 실패한다.")
	void 경매수정_실패케이스_Session이_null인_경우_수정에_실패한다() {
		// given
		String exceptionMessage = "session이 null입니다";
		AuctionDto.ModifyAuctionDto dto = new AuctionDto.ModifyAuctionDto();
		// when
		given(userService.getUserById(null)).willThrow(new NullPointerException(exceptionMessage));
		NullPointerException exception = assertThrows(NullPointerException.class,
			() -> auctionService.modifyAuction(null, fakeAuctionId, dto));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("aucionID가 존재하지않으면 경매 수정에 실패한다.")
	void 경매수정_실패케이스_actionId가_null인_경우_수정에_실패한다() {
		// given
		String exceptionMessage = "해당 경매내역이 존재하지않습니다.";
		AuctionDto.ModifyAuctionDto dto = new AuctionDto.ModifyAuctionDto();
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
		AuctionDto.ModifyAuctionDto dto = new AuctionDto.ModifyAuctionDto();
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
		AuctionDto.ModifyAuctionDto dto = AuctionDto.ModifyAuctionDto.builder()
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
		AuctionDto.ModifyAuctionResponseDto responseDto = auctionService.modifyAuction(fakeSessionId, fakeAuctionId,
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
		given(userService.getUserById(null)).willThrow(new NullPointerException(exceptionMessage));
		NullPointerException exception = assertThrows(NullPointerException.class,
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
		List<AuctionDto.AuctionResponseDto> mockResponse = List.of(
			AuctionDto.AuctionResponseDto.builder()
				.id(1L)
				.title("Auction 1")
				.content("Description 1")
				.status(AuctionStatus.AUCTION)
				.endDate(LocalDateTime.now())
				.startDate(LocalDateTime.now())
				.createdBy(1L)
				.creator(createdBy.getNickname())
				.build(),
			AuctionDto.AuctionResponseDto.builder()
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
		List<AuctionDto.AuctionResponseDto> result = auctionService.findAuctionList();

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