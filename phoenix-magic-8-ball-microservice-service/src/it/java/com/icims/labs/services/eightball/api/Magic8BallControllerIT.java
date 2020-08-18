package com.icims.labs.services.eightball.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;

/**
 * 
 * @author imran.pasha
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class Magic8BallControllerIT {
	
	@Autowired
	private MockMvc mockMvc; 
	
	@Autowired 
	private Magic8BallRepo repo; 
	
	@Before
	public void init() {
		repo.deleteAll();
		assertThat(repo.count()).isZero();
	}
	
	@Test
	public void randomAnswerFetchedWhenSuccessful() throws Exception {
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
				.content(new ObjectMapper().writeValueAsString(buildMockUserRequest()))).andDo(print())
				.andExpect(status().isOk());

		Assertions.assertThat(repo.findByQuestion("Will it rain ?"))
					.anyMatch(history -> history.getQuestion().equals("Will it rain ?") && history.getLanguageCode().equals("en_US"));
	}
	
	@Test
	public void randomAnswerFetchedResponseIsCorrect() throws Exception{
		mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
					.content(new ObjectMapper().writeValueAsString(buildMockUserRequest())))
					.andDo(print())
					.andExpect(status().isOk())
					.andExpect(jsonPath("$.answer").isString());
		
	}
	
	public static UserRequest buildMockUserRequest() {
        Language language = Language.builder().code("en_US").locale("en_US").name("USA").build();
        return UserRequest.builder().question("Will it rain ?").userId(null).language(language).build();
    }
}

interface Magic8BallRepo extends Magic8BallRepository {
	@Query("SELECT m8 FROM History m8 WHERE m8.question =:question")
	List<History> findByQuestion(@Param("question") String question);
}
