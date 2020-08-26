package com.icims.labs.services.eightball.service;

import com.icims.labs.services.eightball.model.SentimentResult;
import com.icims.labs.services.eightball.model.UserRequest;

public interface ComprehendService {
	SentimentResult getQuestionSentiment(UserRequest userRequest);
}

