package com.icims.labs.services.eightball.api;


import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.Magic8BallService;
import com.icims.labs.services.eightball.util.TestUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Magic8BallServiceTests {
    @Autowired
    private Magic8BallService magic8BallService;

    @Mock
    private Magic8BallRepository magic8BallRepository;

    @Test
    public void verifyStringResultWhenGetRandomAnswerIsCalled() {
        Mockito.when(magic8BallRepository.save(TestUtils.buildQuestionHistory(TestUtils.buildMockUserRequest())))
                .thenReturn(TestUtils.buildQuestionHistory(TestUtils.buildMockUserRequest()));
        Assert.assertNotNull(magic8BallService.getRandomAnswer(TestUtils.buildMockUserRequest()));
    }

    @Test
    public void verifyResultWhenGetHistoryIsCalled() {
        Mockito.when(magic8BallRepository.save(TestUtils.buildQuestionHistory(TestUtils.buildMockUserRequest())))
                .thenReturn(TestUtils.buildQuestionHistory(TestUtils.buildMockUserRequest()));
        Mockito.when(magic8BallRepository.findAll())
                .thenReturn(TestUtils.buildHistory());
        magic8BallService.getRandomAnswer(TestUtils.buildMockUserRequest());
        Assert.assertNotNull(magic8BallService.getHistory());
        Assert.assertNotNull(magic8BallService.getHistory().size());
    }


}