package com.icims.labs.services.eightball.api;


import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.Magic8BallService;
import com.icims.labs.services.eightball.util.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class Magic8BallServiceTests {

    @InjectMocks
    private Magic8BallService magic8BallService ;

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
    public void verifyStringResultWhenGetRandomAnswerIsCalled() {
        Mockito.when(magic8BallRepository.save(history)).thenReturn(history);
        Assert.assertNotNull(magic8BallService.getRandomAnswer(userRequest));
    }

    @Test
    public void verifyResultWhenGetHistoryIsCalled() {
        Mockito.when(magic8BallRepository.findAll())
                .thenReturn(historyList);
        Assert.assertNotNull(magic8BallService.getHistory());
        Assert.assertNotNull(magic8BallService.getHistory().size());
    }


}