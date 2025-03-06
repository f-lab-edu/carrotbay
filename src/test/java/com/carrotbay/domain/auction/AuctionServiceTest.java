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
import org.springframework.mock.web.MockHttpSession;

import com.carrotbay.domain.auction.dto.AuctionDto;
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.UserService;
import com.carrotbay.dummy.DummyObject;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest extends DummyObject {

	@InjectMocks
	private AuctionService auctionService;

	@Mock
	private UserService userService;

	@Mock
	private AuctionRepository auctionRepository;

	private String SESSION_KEY = "USER_ID";
	private Long fakeSessionId = 999L;
	private Long fakeAuctionId = 999L;

	@Test
	@DisplayName("session이 null이면 경매 등록에 실패한다.")
	void 경매등록_실패케이스_Session이_null인_경우_등록에_실패한다() {
		// given
		String exceptionMessage = "session이 null입니다";
		AuctionDto.UpsertAuctionDto dto = new AuctionDto.UpsertAuctionDto();
		// when
		given(userService.getUserById(null)).willThrow(new NullPointerException(exceptionMessage));
		// then
		NullPointerException exception = assertThrows(NullPointerException.class,
			() -> auctionService.postAuction(null, dto));
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("사용자가 존재하지않으면 등록에 실패한다.")
	void 경매등록_실패케이스_사용자가_존재하지않는_경우_등록에_실패한다() {
		// given
		AuctionDto.UpsertAuctionDto postDto = new AuctionDto.UpsertAuctionDto();
		HttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute(SESSION_KEY, fakeSessionId);
		// userService.getUserById(session) 호출 시 예외 발생하도록 설정
		given(userService.getUserById(httpSession)).willThrow(new NullPointerException("해당 사용자가 존재하지않습니다."));

		// when & then
		assertThrows(NullPointerException.class, () -> auctionService.postAuction(httpSession, postDto));
	}

	@Test
	@DisplayName("경매등록_성공케이스.")
	void 경매등록_성공케이스() {
		// given
		AuctionDto.UpsertAuctionDto dto = new AuctionDto.UpsertAuctionDto();
		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute(SESSION_KEY, fakeSessionId);

		User user = newMockUser(fakeSessionId, "test");
		Auction auction = newMockAuction(fakeAuctionId, user);
		when(userService.getUserById(any())).thenReturn(user);
		when(auctionRepository.save(any())).thenReturn(auction);
		// when
		Long id = auctionService.postAuction(httpSession, dto);
		// then
		Assertions.assertThat(id).isEqualTo(fakeAuctionId);
	}

	@Test
	@DisplayName("autcion DTO가 null이면 경매 수정에 실패한다.")
	void 경매수정_실패케이스_DTO가_null인_경우_경매수정에_실패한다() {
		// given
		AuctionDto.UpsertAuctionDto dto = null;
		MockHttpSession httpSession = new MockHttpSession();
		httpSession.setAttribute(SESSION_KEY, fakeSessionId);
		// when & then
		assertThrows(IllegalArgumentException.class, () -> auctionService.modifyAuction(httpSession, 1L, dto));
	}

	@Test
	@DisplayName("session이 null이면 경매 수정에 실패한다.")
	void 경매수정_실패케이스_Session이_null인_경우_수정에_실패한다() {
		// given
		String exceptionMessage = "session이 null입니다";
		AuctionDto.UpsertAuctionDto dto = new AuctionDto.UpsertAuctionDto();
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
		AuctionDto.UpsertAuctionDto dto = new AuctionDto.UpsertAuctionDto();
		HttpSession httpSession = new MockHttpSession();
		User user = newMockUser(fakeSessionId, "test");
		// when
		lenient().when(userService.getUserById(any())).thenReturn(user);
		given(auctionRepository.findById(fakeAuctionId)).willThrow(new IllegalArgumentException(exceptionMessage));
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> auctionService.modifyAuction(httpSession, fakeAuctionId, dto));
		// then
		assertEquals(exceptionMessage, exception.getMessage());
	}

	@Test
	@DisplayName("auctionId와 작성자의 id가 틀린 경우 수정에 실패한다.")
	void 경매수정_실패케이스_actionId와_작성자의_id가_틀린_경우_수정에_실패한다() {
		// given
		String exceptionMessage = "작성자가 아닙니다.";
		AuctionDto.UpsertAuctionDto dto = new AuctionDto.UpsertAuctionDto();
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
		int minimumPrice = 100;
		int instantPrice = 2000;
		AuctionDto.UpsertAuctionDto dto = new AuctionDto.UpsertAuctionDto();
		dto.setTitle(title);
		dto.setContent(content);
		dto.setEndDate(endDate);
		dto.setMinimumPrice(minimumPrice);
		dto.setInstantPrice(instantPrice);
		HttpSession httpSession = new MockHttpSession();
		User user = newMockUser(fakeSessionId, title);
		Auction auction = newMockAuction(fakeAuctionId, user);
		lenient().when(userService.getUserById(any())).thenReturn(user);
		lenient().when(auctionRepository.findById(any())).thenReturn(Optional.of(auction));
		lenient().when(auctionRepository.save(any())).thenReturn(auction);
		// // when
		AuctionDto.AuctionResponseDto responseDto = auctionService.modifyAuction(httpSession, fakeAuctionId, dto);
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
		HttpSession httpSession = new MockHttpSession();
		User user = newMockUser(fakeSessionId, "test");
		// when
		lenient().when(userService.getUserById(any())).thenReturn(user);
		given(auctionRepository.findById(fakeAuctionId)).willThrow(new IllegalArgumentException(exceptionMessage));
		IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
			() -> auctionService.deleteAuction(httpSession, fakeAuctionId));
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
		AuctionDto.AuctionRequestDto requestDto = new AuctionDto.AuctionRequestDto(
			1L, "Test Auction", LocalDateTime.now(), LocalDateTime.now(), 0, 10
		);

		List<AuctionDto.AuctionResponseDto> mockResponse = List.of(
			new AuctionDto.AuctionResponseDto(
				1L, "Auction 1", "Description 1", AuctionStatus.AUCTION,
				LocalDateTime.now(), LocalDateTime.now().plusDays(3),
				1000, 5000, null, "user1"
			),
			new AuctionDto.AuctionResponseDto(
				2L, "Auction 2", "Description 2", AuctionStatus.CANCEL,
				LocalDateTime.now().minusDays(5), LocalDateTime.now().minusDays(2),
				2000, 7000, 3L, "user2"
			)
		);

		// Mock 설정
		when(auctionRepository.getAuctionList(requestDto)).thenReturn(mockResponse);

		// When (테스트 실행)
		List<AuctionDto.AuctionResponseDto> result = auctionService.getAuctionList(requestDto);

		// Then (검증)
		assertNotNull(result);
		assertEquals(2, result.size());

		// 첫 번째 응답 값 검증
		assertEquals(1L, result.get(0).getId());
		assertEquals("Auction 1", result.get(0).getTitle());
		assertEquals("Description 1", result.get(0).getContent());
		assertEquals(AuctionStatus.AUCTION.getStatus(), result.get(0).getStatus());
		assertEquals(1000, result.get(0).getMinimumPrice());
		assertEquals(5000, result.get(0).getInstantPrice());
		assertNull(result.get(0).getSuccessfulBidderId());
		assertEquals("user1", result.get(0).getCreatedBy());

		// 두 번째 응답 값 검증
		assertEquals(2L, result.get(1).getId());
		assertEquals("Auction 2", result.get(1).getTitle());
		assertEquals("Description 2", result.get(1).getContent());
		assertEquals(AuctionStatus.CANCEL.getStatus(), result.get(1).getStatus());
		assertEquals(2000, result.get(1).getMinimumPrice());
		assertEquals(7000, result.get(1).getInstantPrice());
		assertEquals(3L, result.get(1).getSuccessfulBidderId());
		assertEquals("user2", result.get(1).getCreatedBy());

		// auctionRepository.getAuctionList가 한 번 호출되었는지 검증
		verify(auctionRepository, times(1)).getAuctionList(requestDto);
	}
}