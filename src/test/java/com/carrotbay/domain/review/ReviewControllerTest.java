package com.carrotbay.domain.review;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.carrotbay.config.EmbeddedRedisConfig;
import com.carrotbay.domain.review.dto.ReviewRequestDto;
import com.carrotbay.domain.review.dto.ReviewResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@SpringBootTest
@Import(EmbeddedRedisConfig.class)
public class ReviewControllerTest {
	@Autowired
	private MockMvc mvc;
	@MockitoBean
	private ReviewService reviewService;
	@Autowired
	private ObjectMapper om;
	private final Long auctionId = 999L;
	private final Long userId = 888L;
	private final Long reviewId = 777L;
	private final String API = "/api/reviews";

	@Test
	@DisplayName("제목이 null이면 후기등록에 실패한다.")
	void 후기등록_실패케이스_제목이_null인_경우() throws Exception {

		// given
		ReviewRequestDto.CreateReviewDto dto = new ReviewRequestDto.CreateReviewDto(null, "content", auctionId);
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post(API)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.title")
				.value("제목을 입력해주세요."));
	}

	@Test
	@DisplayName("제목이 5자 이상 20자 이하라면 후기등록에 실패한다.")
	void 후기등록_실패케이스_제목이_5자이상_20자이하인_경우() throws Exception {
		// given
		ReviewRequestDto.CreateReviewDto dto = new ReviewRequestDto.CreateReviewDto("테스트", "content", auctionId);
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post(API)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.title")
				.value("제목은 5자 이상 20자 이하로 입력해주세요."));
	}

	@Test
	@DisplayName("본문이 null인 경우 후기등록에 실패한다")
	void 후기등록_실패케이스_본문이_null인_경우() throws Exception {
		// given
		ReviewRequestDto.CreateReviewDto dto = new ReviewRequestDto.CreateReviewDto("테스트 5자이다.", null, auctionId);
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.post(API)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content")
				.value("본문을 입력해주세요."));
	}

	@Test
	@DisplayName("후기 등록 성공")
	void 후기등록_성공케이스() throws Exception {
		// given
		ReviewRequestDto.CreateReviewDto dto = new ReviewRequestDto.CreateReviewDto("테스트 5자이다.", "내용을 입력함.", auctionId);
		String requestBody = om.writeValueAsString(dto);
		ReviewResponseDto.PostResponseDto postResponseDto = new ReviewResponseDto.PostResponseDto(reviewId);
		given(reviewService.postReview(any(), any())).willReturn(postResponseDto);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("USER_ID", userId);
		// when
		MvcResult result = mvc.perform(MockMvcRequestBuilders.post(API)
				.session(session)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();
		MockHttpServletResponse httpResponse = result.getResponse();
		ObjectMapper objectMapper = new ObjectMapper();
		ReviewResponseDto.PostResponseDto response = objectMapper
			.readValue(httpResponse.getContentAsString(), ReviewResponseDto.PostResponseDto.class);

		// then
		Assertions.assertEquals(reviewId, response.getId());
	}

	@Test
	@DisplayName("제목이 null이면 후기수정에 실패한다.")
	void 후기수정_실패케이스_제목이_null인_경우() throws Exception {

		// given
		ReviewRequestDto.ModifyReviewDto dto = new ReviewRequestDto.ModifyReviewDto(null, "content");
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.put(API + "/" + reviewId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.title")
				.value("제목을 입력해주세요."));
	}

	@Test
	@DisplayName("제목이 5자 이상 20자 이하라면 후기수정에 실패한다.")
	void 후기수정_실패케이스_제목이_5자이상_20자이하인_경우() throws Exception {
		// given
		ReviewRequestDto.ModifyReviewDto dto = new ReviewRequestDto.ModifyReviewDto("테스트", "content");
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.put(API + "/" + reviewId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.title")
				.value("제목은 5자 이상 20자 이하로 입력해주세요."));
	}

	@Test
	@DisplayName("본문이 null인 경우 후기수정에 실패한다")
	void 후기수정_실패케이스_본문이_null인_경우() throws Exception {
		// given
		ReviewRequestDto.ModifyReviewDto dto = new ReviewRequestDto.ModifyReviewDto("테스트 5자이다.", null);
		String requestBody = om.writeValueAsString(dto);

		// when, then
		mvc.perform(MockMvcRequestBuilders.put(API + "/" + reviewId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().is4xxClientError())
			.andExpect(MockMvcResultMatchers.jsonPath("$.content")
				.value("본문을 입력해주세요."));
	}

	@Test
	@DisplayName("후기 수정 성공")
	void 후기수정_성공케이스() throws Exception {
		// given
		String title = "테스트 5자이다.";
		String content = "내용을 입력함.";
		ReviewRequestDto.ModifyReviewDto dto = new ReviewRequestDto.ModifyReviewDto(title, content);
		String requestBody = om.writeValueAsString(dto);
		ReviewResponseDto.ModifyResponseDto modifyResponseDto = new ReviewResponseDto.ModifyResponseDto(reviewId, title,
			content, null, auctionId, userId, "test", "auction Title");
		given(reviewService.modifyReview(any(), any(), any())).willReturn(modifyResponseDto);
		MockHttpSession session = new MockHttpSession();
		session.setAttribute("USER_ID", userId);
		// when
		MvcResult result = mvc.perform(MockMvcRequestBuilders.put(API + "/" + reviewId)
				.session(session)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(MockMvcResultMatchers.status().isCreated())
			.andReturn();
		MockHttpServletResponse httpResponse = result.getResponse();
		ObjectMapper objectMapper = new ObjectMapper();
		ReviewResponseDto.ModifyResponseDto response = objectMapper
			.readValue(httpResponse.getContentAsString(), ReviewResponseDto.ModifyResponseDto.class);

		// then
		Assertions.assertEquals(reviewId, response.getId());
		Assertions.assertEquals(title, response.getTitle());
		Assertions.assertEquals(content, response.getContent());
		Assertions.assertEquals(auctionId, response.getAuctionId());
		Assertions.assertEquals(userId, response.getUserId());
	}

}
