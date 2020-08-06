package com.icims.labs.services.eightball.api;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@WebMvcTest(RandomAnswerController.class)
public class RandomAnswerControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void receiveStringResultWhenServiceIsInvoked() throws Exception {
		mockMvc.perform(get("/api/helloworld")).andExpect(jsonPath("$.pheonix").value("Hello World from Phoenix!!!"));
	}

	@Test
	public void verifyResponseStatusWhenRequestMethodIsGet() throws Exception {
		mockMvc.perform(get("/api/helloworld")).andExpect(status().isOk());
	}

	@Test
	public void verifyResponseStatusWhenRequestMethodIsPost() throws Exception {
		mockMvc.perform(post("/api/helloworld")).andExpect(status().is4xxClientError());
	}

	@Test
	public void verifyStringResultWhenRandomAnswerServiceIsInvoked() throws Exception {
		mockMvc.perform(post("/api/answer").content("{\"question\":\"Will it rain?\" }")).andExpect(status().isOk())
				.andExpect(jsonPath("$.answer").isString());
	}

	@Test
	public void verifyResponseStatusWhenRandomAnswerIsInvokedViaGet() throws Exception {
		mockMvc.perform(get("/api/answer")).andExpect(status().is4xxClientError());
	}

}
