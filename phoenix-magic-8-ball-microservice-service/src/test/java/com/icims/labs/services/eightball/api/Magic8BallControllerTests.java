package com.icims.labs.services.eightball.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.SentimentAnswer;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.service.Magic8BallService;
import com.icims.labs.services.eightball.util.TestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
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
                .content(new ObjectMapper().writeValueAsString(buildMockUserRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").isString());
    }

    @Test
    public void verifyResultWhenRandomAnswerServiceIsInvoked() throws Exception {
        when(magic8BallService.getRandomAnswer(buildMockUserRequest())).thenReturn("It is likely");

        mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(buildMockUserRequest())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").isString());
    }

    @Test
    public void verifyResultWhenHistoryServiceIsInvoked() throws Exception {
        when(magic8BallService.getHistory()).thenReturn(TestUtils.buildHistory());

        mockMvc.perform(get("/api/history"))
                .andExpect(status().isOk());

    }

    @Test
    public void verifyTrendingQuestionsServiceIsInvoked() throws Exception {
        when(magic8BallService.getTrendingQuestions(anyString())).thenReturn(TestUtils.buildHistory());

        mockMvc.perform(get("/api/trendingQuestions").contentType(MediaType.APPLICATION_JSON).param("languageCode","en-US"))
                .andExpect(status().isOk());
    }

    @Test
    public void verifyTrendingQuestionsServiceIs4XXWhenLanguageIsNotPassed() throws Exception {
        when(magic8BallService.getTrendingQuestions(anyString())).thenReturn(TestUtils.buildHistory());

        mockMvc.perform(get("/api/trendingQuestions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void verifyResponseIs4XXWhenRandomAnswerIsInvokedViaGet() throws Exception {
        mockMvc.perform(get("/api/answer")).andExpect(status().is4xxClientError());
    }

    public static UserRequest buildMockUserRequest() {
        Language language = Language.builder().code("en_US").locale("en_US").name("USA").build();
        return UserRequest.builder().question("Will it rain ?").userId(null).language(language).build();
    }
    
    @Test
    public void responseAnswerWhenPayLoadContainsOnlyQuestionMark() throws Exception
    {
    	mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
    			.content("{\"question\":\"?\",\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
    	.andExpect(status().isOk())
    	.andExpect(jsonPath("$.answer").value("!"));
    }
    
  @Test
    public void responseAnswerWhenPayloadEndsWithQuestionMark() throws Exception
    {
    	when(magic8BallService.getRandomAnswer(buildMockUserRequest())).thenReturn("It is likely");
    	
    	mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
    			.content("{\"question\":\"Will it rain today\",\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
    	        .andExpect(status().isOk());
    	        
    }
   
  @Test
  public void responseAnswerWhenPayLoadContainsNull() throws Exception
  {
  	mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
  			.content("{\"question\":null,\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
  	.andExpect(status().isOk());
  	
  }
  
  @Test
  public void responseAnswerWhenPayLoadContainsToomuchLength() throws Exception
  {
  	mockMvc.perform(post("/api/answer").contentType(MediaType.APPLICATION_JSON)
  			.content("{\"question\":\"Will it rain todayyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy?\",\"language\":{\"locale\":\"en-US\",\"code\":\"en-US\",\"name\":\"USA\"},\"userId\":\"dummy\"}"))
  	.andExpect(status().isOk());
  	
  }
}
