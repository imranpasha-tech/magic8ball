package com.icims.labs.services.eightball.api;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.icims.labs.services.eightball.service.RandomAnswerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RandomAnswerServiceTests {
	@Autowired
	private RandomAnswerService randomAnswerService;
	
	@Test
	public void verifyStringResultWhenGetRandomAnswerIsCalled() {
		Assert.assertTrue(randomAnswerService.getRandomAnswer() instanceof String);
	}
	
}