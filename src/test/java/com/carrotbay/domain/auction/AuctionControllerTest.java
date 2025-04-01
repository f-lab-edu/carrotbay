package com.carrotbay.domain.auction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.carrotbay.domain.auction.dto.AuctionRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
class AuctionControllerTest {
	@Autowired
	private MockMvc mvc;
	@Autowired
	private ObjectMapper om;

	@Test
	@DisplayName("제목이 null이면 입력에 실패한다.")
	void 경매등록_실패케이스_제목이_null인_경우() throws Exception {

		// given
		AuctionRequestDto.CreateAuctionDto auctionDto = new AuctionRequestDto.CreateAuctionDto();
		String requestBody = om.writeValueAsString(auctionDto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post("/api/auctions")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.title")
				.value("제목을 입력해주세요."));
	}

	@Test
	@DisplayName("제목이 5-20자가 아니면 입력에 실패한다.")
	void 경매등록_실패케이스_제목이_5_20자가_아닌_경우() throws Exception {

		// given
		AuctionRequestDto.CreateAuctionDto auctionDto = AuctionRequestDto.CreateAuctionDto.builder()
			.title("test")
			.build();
		String requestBody = om.writeValueAsString(auctionDto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post("/api/auctions")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.title")
				.value("제목은 5자 이상 20자 이하로 입력해주세요."));
	}

	@Test
	@DisplayName("본문이 null인 경우 입력에 실패한다.")
	void 경매등록_실패케이스_본문이_null인_경우() throws Exception {

		// given
		AuctionRequestDto.CreateAuctionDto auctionDto = new AuctionRequestDto.CreateAuctionDto();
		String requestBody = om.writeValueAsString(auctionDto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post("/api/auctions")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content")
				.value("본문을 입력해주세요."));
	}

	@Test
	@DisplayName("종료날짜가 null인 경우 입력에 실패한다.")
	void 경매등록_실패케이스_종료날짜가_null인_경우() throws Exception {

		// given
		AuctionRequestDto.CreateAuctionDto auctionDto = new AuctionRequestDto.CreateAuctionDto();
		String requestBody = om.writeValueAsString(auctionDto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post("/api/auctions")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.endDate")
				.value("경매 종료날짜를 입력해주세요."));
	}

	@Test
	@DisplayName("최소 가격이 0이면 입력에 실패한다.")
	void 경매등록_실패케이스_최소가격이_0인_경우() throws Exception {

		// given
		AuctionRequestDto.CreateAuctionDto auctionDto = new AuctionRequestDto.CreateAuctionDto();
		String requestBody = om.writeValueAsString(auctionDto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post("/api/auctions")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.minimumPrice")
				.value("최소 가격은 0원이 될 수 없습니다."));
	}

	@Test
	@DisplayName("즉시 낙찰 가격이 0이면 입력에 실패한다.")
	void 경매등록_실패케이스_즉시낙찰가격이_0인_경우() throws Exception {

		// given
		AuctionRequestDto.CreateAuctionDto auctionDto = new AuctionRequestDto.CreateAuctionDto();
		String requestBody = om.writeValueAsString(auctionDto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post("/api/auctions")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.instantPrice")
				.value("즉시 낙찰 가격은 0원이 될 수 없습니다."));
	}
}