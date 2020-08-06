package com.icims.labs.services.eightball.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Random answers controller for integration testing.
 * 
 * @author imran.pasha
 */
@RestController
@RequestMapping("/api")
public class RandomAnswerController {
	private static final Logger logger = LoggerFactory.getLogger(RandomAnswerController.class);
	
	
	// dummy service
	@GetMapping("/helloworld")
	public Map<String, String> helloWorld() {
		logger.info("Successfully received request for HelloWorld.");

		Map<String, String> helloWorld = new HashMap<String, String>();
		helloWorld.put("pheonix", "Hello World from Phoenix!!!");

		return helloWorld;
	}
	
	
	//Find the answer for the question from the set of predefined answers.
	@PostMapping("/answer")
	public Map<String, String> randomAnswer(@RequestBody String question) {
		logger.info("Fetching a random answer.");
		
		Map<String, String> answer = new HashMap<String, String>();
		answer.put("answer", getRandomAnswer());
		
		return answer;
	}
	
	/**
	 * Utility method that gives random answers.
	 * 
	 * @return String
	 */
	private String getRandomAnswer() {
	    String[] answers = {
	            "Most Likely",
	            "As I see it yes",
	            "Outlook Good",
	            "Looking like no",
	            "Very doubtful",
	            "Reply hazy, try again"
	    };
	    String randomAnswer = answers[(new Random()).nextInt(answers.length)];
	    return randomAnswer;
	}
}
