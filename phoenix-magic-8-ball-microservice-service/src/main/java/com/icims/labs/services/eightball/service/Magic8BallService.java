package com.icims.labs.services.eightball.service;

import com.icims.labs.services.eightball.model.SentimentAnswer;
import com.icims.labs.services.eightball.model.UserRequest;

public interface Magic8BallService {

	SentimentAnswer getRandomAnswer(UserRequest userRequest);
}
