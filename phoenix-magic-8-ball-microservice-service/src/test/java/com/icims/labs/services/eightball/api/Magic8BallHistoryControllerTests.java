package com.icims.labs.services.eightball.api;

import com.icims.labs.services.eightball.service.Magic8BallHistoryService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(Magic8BallHistoryController.class)
public class Magic8BallHistoryControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private Magic8BallHistoryService magic8BallHistoryService;


    @Test
    public void verifyResultWhenHistoryServiceIsInvoked() throws Exception {
        when(magic8BallHistoryService.getHistory()).thenReturn(TestUtils.buildHistory());

        mockMvc.perform(get("/api/history"))
                .andExpect(status().isOk());

    }

    @Test
    public void verifyTrendingQuestionsServiceIsInvoked() throws Exception {
        when(magic8BallHistoryService.getTrendingQuestions(anyString())).thenReturn(TestUtils.buildHistory());

        mockMvc.perform(get("/api/trendingQuestions").contentType(MediaType.APPLICATION_JSON).param("languageCode","en-US"))
                .andExpect(status().isOk());
    }

    @Test
    public void verifyTrendingQuestionsServiceIs4XXWhenLanguageIsNotPassed() throws Exception {
        when(magic8BallHistoryService.getTrendingQuestions(anyString())).thenReturn(TestUtils.buildHistory());

        mockMvc.perform(get("/api/trendingQuestions").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

}
