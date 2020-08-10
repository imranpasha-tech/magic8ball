package com.icims.labs.services.eightball.service;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import com.icims.labs.services.eightball.api.RandomAnswerController;

@Service
public class RandomAnswerService {
	private static final Logger logger = LoggerFactory.getLogger(RandomAnswerService.class);
	
	/**
	 * Service method that selects a random mocked answer.
	 * 
	 * @return String
	 */
	public String getRandomAnswer() {
		String[] answers = { 
				"Most Likely", 
				"As I see it yes", 
				"Outlook Good", 
				"Looking like no", 
				"Very doubtful",
				"Reply hazy, try again"
				};
		String randomAnswer = answers[(new Random()).nextInt(answers.length)];
		
		logger.info("Random answer is fetched: {}", randomAnswer);
		
		return randomAnswer;
	}
}
