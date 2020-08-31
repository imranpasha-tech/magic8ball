package com.icims.labs.services.eightball.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import javax.validation.ValidationException;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.SentimentResult;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.service.impl.ComprehendServiceImpl;

@ExtendWith(MockitoExtension.class)
public class ComprehendServiceTests {
	@InjectMocks
	private ComprehendServiceImpl comprehendService;

	@Mock
	private AmazonComprehend comprehend;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void checkSentimentIsDetected() {
		String question = "Should we dance?";
		DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest().withText(question).withLanguageCode("en");
		DetectSentimentResult sentimentResult = new DetectSentimentResult();
		sentimentResult.setSentiment("POSITIVE");
		
		when(comprehend.detectSentiment(detectSentimentRequest)).thenReturn(sentimentResult);

		SentimentResult result = comprehendService.getQuestionSentiment(
				UserRequest.builder().question(question).language(Language.builder().code("en").build()).build());

		Assertions.assertNotNull(result);
		Assertions.assertEquals("POSITIVE", result.getSentiment());
	}
	
	@Test 
	public void checkExceptionThrownWhenQuestionIsNull() {
		UserRequest request = UserRequest.builder().question(null).build();
		
		assertThrows(IllegalArgumentException.class, () -> comprehendService.getQuestionSentiment(request));
	}
	
	@Test 
	public void checkExceptionThrownWhenUserRequestIsNull() {
		UserRequest request = null;
		assertThrows(IllegalArgumentException.class, () -> comprehendService.getQuestionSentiment(request));
	}
	
	@Test
	public void checkComprehendExceptionWhenLangCodeIsInvalid() {
		String question = "Should we dance?";
		DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest().withText(question).withLanguageCode("en-US");
		UserRequest request = UserRequest.builder().question(question).language(Language.builder().code("en-US").build()).build();
		
		when(comprehend.detectSentiment(detectSentimentRequest)).thenThrow(ValidationException.class);
		
		Assertions.assertThrows(ValidationException.class, () -> comprehendService.getQuestionSentiment(request));
	}
}
