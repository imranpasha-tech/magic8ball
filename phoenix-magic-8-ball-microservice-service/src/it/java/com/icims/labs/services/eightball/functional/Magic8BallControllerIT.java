package com.icims.labs.services.eightball.functional;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icims.labs.services.eightball.api.Magic8BallController;
import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.service.Magic8BallService;
import com.icims.labs.services.eightball.utility.AbstractDataTestContainer;
import com.icims.labs.services.eightball.utility.Magic8BallRepo;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

/**
 * This class performs isolated functional testing of {@link Magic8BallController} and {@link Magic8BallService}
 * 
 * @author imran.pasha {@literal ipasha@icims.com}
 *
 */
@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Magic8BallControllerIT extends AbstractDataTestContainer{

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private Magic8BallRepo repo;

	@Autowired
	private ObjectMapper objectMapper;

	@Before
	public void init() {
		repo.deleteAll();
		assertThat(repo.count()).isZero();
	}
	
	@Test
	public void randomAnswerSuccessfulWhenFieldsAreValid() throws Exception {
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(buildMockUserRequest()))).andDo(print())
				.andExpect(status().isOk());

		Assertions.assertThat(repo.findByQuestion("Will it rain ?")).anyMatch(
				history -> history.getQuestion().equals("Will it rain ?") && history.getLanguageCode().equals("en_US"));
	}

	@Test
	public void randomAnswerFetchedResponseIsCorrect() throws Exception {
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(buildMockUserRequest()))).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.answer").isString());
	}

	@Test
	public void randomAnswerFetchedResponseIsNotEmpty() throws Exception {
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(buildMockUserRequest()))).andDo(print())
				.andExpect(status().isOk()).andExpect(jsonPath("$.answer").isNotEmpty());
	}

	@Test
	public void randomAnswerFetchedResponseIsValidString() throws Exception {
		MvcResult result = mockMvc
				.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(buildMockUserRequest())))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		JsonNode resultNode = objectMapper.readTree(result.getResponse().getContentAsString());
		String answer = resultNode.get("answer").asText();

		Assertions.assertThat(answer).isNotEqualTo("");
	}

	@Test
	public void randomAnswerFetchedResponseIsOneOfTheValidAnswers() throws Exception {
		MvcResult result = mockMvc
				.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
						.content(new ObjectMapper().writeValueAsString(buildMockUserRequest())))
				.andDo(print()).andExpect(status().isOk()).andReturn();

		JsonNode resultNode = objectMapper.readTree(result.getResponse().getContentAsString());
		String answer = resultNode.get("answer").asText();

		assertTrue(checkEnumAnswers(answer));
	}
	
	@Test
	public void randomAnswerFailsToPersistOnQuestionLengthAbove120Long() throws Exception {
		String question = "blahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahahblahblahah";
		Language language = Language.builder().code("en_US").locale("en_US").name("USA").build(); 
		UserRequest request = UserRequest.builder().question(question).userId(null).language(language).build();
		
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))).andDo(print())
				.andExpect(status().isOk());
		
		Assertions.assertThat(repo.findByQuestion("Will it rain ?")).noneMatch(history -> history.getQuestion().equals(question));
	}
	
	@Test
	public void randomAnswerFailsToPersistOnQuestionLengthOne() throws Exception {
		String question = "?";
		Language language = Language.builder().code("en_US").locale("en_US").name("USA").build(); 
		UserRequest request = UserRequest.builder().question(question).userId(null).language(language).build();
		
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(request))).andDo(print())
				.andExpect(status().isOk());
		
		Assertions.assertThat(repo.findByQuestion("?")).noneMatch(history -> history.getQuestion().equals(question));
	}
	
	@Test
	public void createTrendingQuestionsWhenValid() throws Exception {
		mockMvc.perform(get("/api/trendingQuestions").contentType(MediaType.APPLICATION_JSON).param("languageCode","en-US"))
				.andExpect(status().isOk());
	}

	private boolean checkEnumAnswers(String answer) {
		for (Answers ans : Answers.values()) {
			if (ans.getAnswerKey().equals(answer)) {
				System.out.println("Answers keys: " + ans.getAnswerKey());
				return true;
			}
		}
		return false;
	}

	private static UserRequest buildMockUserRequest() {
		Language language = Language.builder().code("en_US").locale("en_US").name("USA").build();
		return UserRequest.builder().question("Will it rain ?").userId(null).language(language).build();
	}
}

