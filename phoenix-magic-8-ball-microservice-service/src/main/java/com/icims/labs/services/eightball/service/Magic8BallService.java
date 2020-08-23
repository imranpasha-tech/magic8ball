package com.icims.labs.services.eightball.service;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.UserRequest;

import java.util.List;

public interface Magic8BallService {

	String getRandomAnswer(UserRequest userRequest);

	List<History> getHistory();

	List<History> getTrendingQuestions(String languageCode);
}
