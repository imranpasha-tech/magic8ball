package com.icims.labs.services.eightball.api;


import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
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

import java.util.Optional;


@ExtendWith(MockitoExtension.class)
public class Magic8BallServiceTests {

    @InjectMocks
    private Magic8BallServiceImpl magic8BallService;

    @Mock
    private Magic8BallRepository magic8BallRepository;

    private static UserRequest userRequest;

    private static History history;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        userRequest = TestUtils.buildMockUserRequest();
        history = TestUtils.buildQuestionHistory(userRequest);
    }

    @Test
    public void verifyStringResultWhenGetRandomAnswerIsCalled() {
        Mockito.when(magic8BallRepository.save(history)).thenReturn(history);
        Assert.assertNotNull(magic8BallService.getRandomAnswer(userRequest));
    }

    @Test
    public void verifyFrequencyIsOneIfQuestionDoesnotExists() {
        Mockito.when(magic8BallRepository.findByTruncatedQuestion("willitrain?", "en-US")).thenReturn(Optional.empty());
        Mockito.when(magic8BallRepository.save(history)).thenReturn(history);
        Assert.assertNotNull(magic8BallService.getRandomAnswer(userRequest));
        Assert.assertEquals(1, history.getFrequency());
    }

    @Test
    public void verifyFrequencyIsIncrementedIfQuestionExists() {
        Optional<History> optionalHistory = Optional.of(history);
        Mockito.when(magic8BallRepository.findByTruncatedQuestion("willitrain?", "en_US")).thenReturn(optionalHistory);
        Mockito.when(magic8BallRepository.save(optionalHistory.get())).thenReturn(optionalHistory.get());
        Assert.assertNotNull(magic8BallService.getRandomAnswer(userRequest));
        Assert.assertEquals(2, optionalHistory.get().getFrequency());
    }
}