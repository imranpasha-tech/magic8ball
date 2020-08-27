package com.icims.labs.services.eightball.api;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.impl.Magic8BallHistoryServiceImpl;
import com.icims.labs.services.eightball.service.impl.Magic8BallServiceImpl;
import com.icims.labs.services.eightball.util.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class Magic8BallHistoryServiceTests {

    @InjectMocks
    private Magic8BallHistoryServiceImpl magic8BallHistoryService;

    @Mock
    private Magic8BallRepository magic8BallRepository;

    private static UserRequest userRequest;

    private static History history;

    private static List<History> historyList;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        historyList = TestUtils.buildHistory();
        userRequest = TestUtils.buildMockUserRequest();
        history = TestUtils.buildQuestionHistory(userRequest);
    }

    @Test
    public void verifyResultWhenGetHistoryIsCalled() {
        Mockito.when(magic8BallRepository.findAll())
                .thenReturn(historyList);
        Assert.assertNotNull(magic8BallHistoryService.getHistory());
        Assert.assertNotNull(magic8BallHistoryService.getHistory().size());
    }

    @Test
    public void verifyResultWhenGetTrendingQuestionsIsCalled() {
        Mockito.when(magic8BallRepository.getTrendingQuestionsByLanguage("en-US", PageRequest.of(0, 25)))
                .thenReturn(historyList);
        Assert.assertNotNull(magic8BallHistoryService.getTrendingQuestions("en-US"));
    }
}
