package com.icims.labs.services.eightball.service;

import java.util.List;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.UserRequest;

public interface Magic8BallService {
	public String getRandomAnswer(UserRequest userRequest);
	public List<History> getHistory();
}
