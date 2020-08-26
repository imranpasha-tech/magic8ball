package com.icims.labs.services.eightball.service;

import com.icims.labs.services.eightball.model.UserRequest;


/**
 * Service method that selects a random mocked answer.
 *
 * @return String
 */

public interface Magic8BallService {

	String getRandomAnswer(UserRequest userRequest);

}
