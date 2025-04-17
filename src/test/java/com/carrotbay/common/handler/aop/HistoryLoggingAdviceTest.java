package com.carrotbay.common.handler.aop;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.carrotbay.domain.auction.Auction;
import com.carrotbay.domain.auction.AuctionService;
import com.carrotbay.domain.auction.dto.AuctionRequestDto;
import com.carrotbay.domain.auction.repository.AuctionRepository;
import com.carrotbay.domain.history.HistoryService;
import com.carrotbay.domain.history.dto.HistoryRequestDto;
import com.carrotbay.domain.user.User;
import com.carrotbay.domain.user.repository.UserRepository;
import com.carrotbay.dummy.DummyObject;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class HistoryLoggingIntegrationTest extends DummyObject {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper om;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AuctionRepository auctionRepository;

	@MockitoBean
	private HistoryService historyService;

	private User user;
	private Auction auction;

	@MockitoSpyBean
	AuctionService auctionService;

	@BeforeEach
	void setUp() {
		User testUser = newUser("auction");
		user = userRepository.save(testUser);
		Auction testAuction = newAuction(user);
		auction = auctionRepository.save(testAuction);
	}

	@AfterEach
	void cleanup() {
		auctionRepository.delete(auction);
		userRepository.delete(user);
	}

	@DisplayName("PutMapping 호출 시 AOP를 사용하여 history 내역을 저장한다.")
	@Test
	void 히스토리_저장() throws Exception {
		String title = "Test 4자 이상 입니다.";
		String content = "test";

		// given
		AuctionRequestDto.ModifyAuctionDto auctionDto = AuctionRequestDto.ModifyAuctionDto.builder()
			.title(title)
			.content(content)
			.endDate(LocalDateTime.now())
			.instantPrice(100)
			.minimumPrice(2999)
			.build();
		String requestBody = om.writeValueAsString(auctionDto);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("USER_ID", user.getId());

		doReturn(null).when(auctionService).modifyAuction(any(), any(), any());
		doNothing().when(historyService).saveHistory(any(HistoryRequestDto.CreateHistoryDto.class));

		// when
		mockMvc.perform(MockMvcRequestBuilders.put("/api/auctions/" + auction.getId())
				.session(session)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

		// then
		ArgumentCaptor<HistoryRequestDto.CreateHistoryDto> captor = ArgumentCaptor.forClass(
			HistoryRequestDto.CreateHistoryDto.class);
		verify(historyService, times(1)).saveHistory(captor.capture());
		verify(historyService).saveHistory(captor.capture());
		HistoryRequestDto.CreateHistoryDto history = captor.getValue();
		assertEquals(auction.getId(), history.getEntityId());
		assertEquals("Auction", history.getTableName());
		assertEquals("update", history.getOperation());
		assertTrue(history.getBeforeValue().contains(auction.getContent()));
		assertTrue(history.getAfterValue().contains(content));
	}
}
