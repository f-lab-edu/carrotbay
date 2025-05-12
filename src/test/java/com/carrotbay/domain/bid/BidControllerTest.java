package com.carrotbay.domain.bid;

import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.carrotbay.domain.bid.dto.BidRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
class BidControllerTest {
	@Autowired
	private MockMvc mvc;
	@MockitoBean
	private BidService bidService;
	@Autowired
	private ObjectMapper om;
	private final Long auctionId = 999L;
	private final Long userId = 888L;
	private final Long bidId = 777L;
	private final String API = "/api/bids";

	@Test
	@DisplayName("입찰가가 0이면 입찰에 실패한다.")
	void 입찰등록_실패케이스_입찰가가_0인_경우() throws Exception {

		// given
		BidRequestDto.CreateBidDto dto = new BidRequestDto.CreateBidDto(0, auctionId);
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post(API)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.bidPrice")
				.value("입찰 가격은 0원이 될 수 없습니다."));
	}

	@Test
	@DisplayName("입찰등록 성공.")
	void 입찰등록_성공케이스() throws Exception {

		// given
		BidRequestDto.CreateBidDto dto = new BidRequestDto.CreateBidDto(100, auctionId);
		String requestBody = om.writeValueAsString(dto);
		given(bidService.postBid(any(), any()))
			.willReturn(bidId);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("USER_ID", userId);

		// when
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(API)
				.session(session)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.sessionAttr("userId", userId))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();
		MockHttpServletResponse response = result.getResponse();
		String responseBody = response.getContentAsString();

		// then
		Assertions.assertEquals(String.valueOf(bidId), responseBody);
	}

	@Test
	@DisplayName("auctionID가 null이면 실패한다.")
	void 입찰수정_실패케이스_auctionId가_null인_경우() throws Exception {

		// given
		BidRequestDto.DeleteBidDto dto = new BidRequestDto.DeleteBidDto(null);
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.put(API + "/" + auctionId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.auctionId")
				.value("경매 id를 입력해주세요."));
	}

	@Test
	@DisplayName("입찰삭제 성공.")
	void 입찰수정_성공케이스() throws Exception {

		// given
		BidRequestDto.DeleteBidDto dto = new BidRequestDto.DeleteBidDto(auctionId);
		String requestBody = om.writeValueAsString(dto);
		given(bidService.cancelBid(any(), any(), any()))
			.willReturn(true);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("USER_ID", userId);

		// when
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(API + "/" + auctionId)
				.session(session)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.sessionAttr("userId", userId))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();
		MockHttpServletResponse response = result.getResponse();
		String responseBody = response.getContentAsString();

		// then
		Assertions.assertEquals(String.valueOf(true), responseBody);
	}
}