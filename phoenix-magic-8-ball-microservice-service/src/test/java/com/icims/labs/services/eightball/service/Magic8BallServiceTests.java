package com.icims.labs.services.eightball.service;


import com.amazonaws.services.comprehend.model.SentimentScore;
import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.SentimentAnswer;
import com.icims.labs.services.eightball.model.SentimentResult;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.impl.Magic8BallServiceImpl;
import com.icims.labs.services.eightball.util.TestUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.when;

import java.util.List;


@ExtendWith(MockitoExtension.class)
public class Magic8BallServiceTests {

    @InjectMocks
    private Magic8BallServiceImpl magic8BallService;

    @Mock
    private Magic8BallRepository magic8BallRepository;
    
    @Mock 
    private ComprehendService comprehendService;

    private static UserRequest userRequest;

    private static History history;

    private static List<History> historyList;
    
    private static SentimentResult sentimentResult;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        historyList = TestUtils.buildHistory();
        userRequest = TestUtils.buildMockUserRequest();
        history = TestUtils.buildQuestionHistory(userRequest);
        sentimentResult = TestUtils.buildSentimentResult(userRequest);
    }

    @Test
    public void verifyStringResultWhenGetRandomAnswerIsCalled() {
        Mockito.when(magic8BallRepository.save(history)).thenReturn(history);
        Mockito.when(comprehendService.getQuestionSentiment(userRequest)).thenReturn(sentimentResult);
        Assert.assertNotNull(magic8BallService.getRandomAnswer(userRequest));
    }
    
    @Test
    public void verifyMixedSentimentIsPositiveResult() {
    	UserRequest request = UserRequest.builder().question("will I dance?").language(Language.builder().code("en").build()).build();
    	SentimentScore score = new SentimentScore();
    	score.setPositive(0.5999f);
    	score.setNegative(0.0235f);
    	score.setNeutral(0.0456f);
    	
    	SentimentResult result = SentimentResult.builder().sentiment("MIXED").score(score).build();
    	when(comprehendService.getQuestionSentiment(request)).thenReturn(result);
    	
    	SentimentAnswer answer = magic8BallService.getRandomAnswer(request);
    	
    	Assertions.assertEquals("POSITIVE", answer.getSentimentResult().getSentiment());
    }
    
    @Test
    public void verifyMixedSentimentIsNegativeResult() {
    	UserRequest request = UserRequest.builder().question("will I dance?").language(Language.builder().code("en").build()).build();
    	SentimentScore score = new SentimentScore();
    	score.setPositive(0.0999f);
    	score.setNegative(0.2355f);
    	score.setNeutral(0.0456f);
    	
    	SentimentResult result = SentimentResult.builder().sentiment("MIXED").score(score).build();
    	when(comprehendService.getQuestionSentiment(request)).thenReturn(result);
    	
    	SentimentAnswer answer = magic8BallService.getRandomAnswer(request);
    	
    	Assertions.assertEquals("NEGATIVE", answer.getSentimentResult().getSentiment());
    }
    
    @Test
    public void verifyMixedSentimentIsNeutralResult() {
    	UserRequest request = UserRequest.builder().question("will I dance?").language(Language.builder().code("en").build()).build();
    	SentimentScore score = new SentimentScore();
    	score.setPositive(0.0999f);
    	score.setNegative(0.0999f);
    	score.setNeutral(0.0999f);
    	
    	SentimentResult result = SentimentResult.builder().sentiment("MIXED").score(score).build();
    	when(comprehendService.getQuestionSentiment(request)).thenReturn(result);
    	
    	SentimentAnswer answer = magic8BallService.getRandomAnswer(request);
    	
    	Assertions.assertEquals("NEUTRAL", answer.getSentimentResult().getSentiment());
    }
    
    @Test
    public void verifyPositiveSentimentResult() {
    	UserRequest request = UserRequest.builder().question("will I dance?").language(Language.builder().code("en").build()).build();
    	SentimentResult result = SentimentResult.builder().sentiment("POSITIVE").build();
    	
    	when(comprehendService.getQuestionSentiment(request)).thenReturn(result);
    	
    	SentimentAnswer answer = magic8BallService.getRandomAnswer(request);
    	
    	Assertions.assertEquals("POSITIVE", answer.getSentimentResult().getSentiment());
    }
    
    @Test
    public void verifyNegativeSentimentResult() {
    	UserRequest request = UserRequest.builder().question("will you die?").language(Language.builder().code("en").build()).build();
    	SentimentResult result = SentimentResult.builder().sentiment("NEGATIVE").build();
    	
    	when(comprehendService.getQuestionSentiment(request)).thenReturn(result);
    	
    	SentimentAnswer answer = magic8BallService.getRandomAnswer(request);
    	
    	Assertions.assertEquals("NEGATIVE", answer.getSentimentResult().getSentiment());
    }
    
    @Test
    public void verifyNeutralSentimentResult() {
    	UserRequest request = UserRequest.builder().question("Should I eat that?").language(Language.builder().code("en").build()).build();
    	SentimentResult result = SentimentResult.builder().sentiment("NEUTRAL").build();
    	
    	when(comprehendService.getQuestionSentiment(request)).thenReturn(result);
    	
    	SentimentAnswer answer = magic8BallService.getRandomAnswer(request);
    	
    	Assertions.assertEquals("NEUTRAL", answer.getSentimentResult().getSentiment());
    }
    
}