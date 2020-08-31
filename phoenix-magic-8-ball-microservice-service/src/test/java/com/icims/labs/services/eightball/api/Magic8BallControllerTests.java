package com.icims.labs.services.eightball.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.SentimentAnswer;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.service.Magic8BallService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Magic8BallController.class)
public class Magic8BallControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private Magic8BallService magic8BallService;

	@Test
	public void verifyStringResultWhenRandomAnswerServiceIsInvoked() throws Exception {
		SentimentAnswer answer = SentimentAnswer.builder().answer("It is likely").build();
		when(magic8BallService.getRandomAnswer(buildMockUserRequest())).thenReturn(answer);

		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(buildMockUserRequest()))).andExpect(status().isOk())
				.andExpect(jsonPath("$.answer").isString());
	}

	@Test
	public void verifyResultWhenRandomAnswerServiceIsInvoked() throws Exception {
		SentimentAnswer answer = SentimentAnswer.builder().answer("It is likely").build();
        when(magic8BallService.getRandomAnswer(buildMockUserRequest())).thenReturn(answer);

		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(buildMockUserRequest()))).andExpect(status().isOk())
				.andExpect(jsonPath("$.answer").isString());
	}

	@Test
	public void verifyResponseIs4XXWhenRandomAnswerIsInvokedViaGet() throws Exception {
		mockMvc.perform(get("/api/answer")).andExpect(status().is4xxClientError());
	}
   

	public static UserRequest buildMockUserRequest() {
		Language language = Language.builder().code("en_US").locale("en_US").name("USA").build();
		return UserRequest.builder().question("Will it rain ?").userId("anonymous").language(language).build();
	}

	@Test
	public void responseAnswerWhenPayLoadContainsOnlyQuestionMark() throws Exception {
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON).content(
				"{\"question\":\"?\",\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.answer").value("try_later"));
	}

	@Test
	public void responseAnswerWhenPayloadEndsWithQuestionMark() throws Exception {
		SentimentAnswer answer = SentimentAnswer.builder().answer("It is likely").build();
		when(magic8BallService.getRandomAnswer(buildMockUserRequest())).thenReturn(answer);

		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON).content(
				"{\"question\":\"Will it rain today\",\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.answer").value("try_later"));

	}

	@Test
	public void responseAnswerWhenPayLoadContainsNull() throws Exception {
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON).content(
				"{\"question\":null,\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
				.andExpect(status().is4xxClientError());

	}

	@Test
	public void responseAnswerWhenPayLoadContainsToomuchLength() throws Exception {
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON).content(
				"{\"question\":\"Will it rain todayyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy?\",\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
				.andExpect(status().isBadRequest()).andExpect(jsonPath("$.answer").value("try_later"));

	}
}
