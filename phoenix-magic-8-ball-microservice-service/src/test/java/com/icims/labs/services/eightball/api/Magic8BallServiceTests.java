package com.icims.labs.services.eightball.api;


import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.UserRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.icims.labs.services.eightball.service.Magic8BallService;


@RunWith(SpringRunner.class)
@SpringBootTest
public class Magic8BallServiceTests {
	@Autowired
	private Magic8BallService magic8BallService;
	
	@Test
	public void verifyStringResultWhenGetRandomAnswerIsCalled() {
		Assert.assertNotNull(magic8BallService.getRandomAnswer(buildMockUserRequest()));
	}

	@Test
	public void verifyResultWhenGetHistoryIsCalled(){
		magic8BallService.getRandomAnswer(buildMockUserRequest());
		Assert.assertNotNull(magic8BallService.getHistory());
		Assert.assertNotNull(magic8BallService.getHistory().size());
	}

	public static UserRequest buildMockUserRequest(){
		Language language = Language.builder().code("en_US").locale("en_US").name("USA").build();
		return UserRequest.builder().question("Will it rain ?").userId("").language(language).build();
	}

}