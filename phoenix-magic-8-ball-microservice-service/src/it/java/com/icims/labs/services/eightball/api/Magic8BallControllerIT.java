package com.icims.labs.services.eightball.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.icims.labs.services.eightball.api.utility.Magic8BallRepo;
import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.Magic8BallService;

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
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This class performs isolated functional testing of {@link Magic8BallController} and {@link Magic8BallService}
 * 
 * @author imran.pasha {@literal ipasha@icims.com}
 *
 */
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(initializers = { Magic8BallControllerIT.Initializer.class })
@Testcontainers
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Magic8BallControllerIT {

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

	@ClassRule
	public static PostgreSQLContainer postgreSQLContainer = new PostgreSQLContainer("postgres")
			.withDatabaseName("phoenix_magic_8_ball").withUsername("postgres").withPassword("root");

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
		public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
			TestPropertyValues
					.of("spring.datasource.url=" + postgreSQLContainer.getJdbcUrl(),
							"spring.datasource.username=" + postgreSQLContainer.getUsername(),
							"spring.datasource.password=" + postgreSQLContainer.getPassword())
					.applyTo(configurableApplicationContext.getEnvironment());
		}
	}
	
	
	@Test
	public void randomAnswerFetchedWhenSuccessful() throws Exception {
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

