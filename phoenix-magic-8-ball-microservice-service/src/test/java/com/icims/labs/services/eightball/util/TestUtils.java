package com.icims.labs.services.eightball.util;

import com.amazonaws.services.comprehend.model.SentimentScore;
import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.SentimentResult;
import com.icims.labs.services.eightball.model.UserRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.mockito.Mock;

public class TestUtils {
	@Mock
	private static SentimentScore score; 
	
    public static List<History> buildHistory() {
        List<History> history = new ArrayList<>();
        history.add(History.builder().question("Will it rain?").languageCode("en").createdDate(LocalDateTime.now()).frequency(3).build());
        return history;
    }

    public static UserRequest buildMockUserRequest() {
        Language language = Language.builder().code("en_US").locale("en_US").name("USA").build();
        return UserRequest.builder().question("Will it rain ?").userId("").language(language).build();
    }

    public static History buildQuestionHistory(UserRequest userRequest) {
        return History.builder().question(userRequest.getQuestion())
                .frequency(1)
                .languageCode(userRequest.getLanguage().getCode())
                .createdDate(LocalDateTime.now())
                .build();
    }
    
    public static SentimentResult buildSentimentResult(UserRequest userRequest) {
    	SentimentResult result = SentimentResult.builder().sentiment("POSITIVE").score(score).build();
    	return result;
    }
}
