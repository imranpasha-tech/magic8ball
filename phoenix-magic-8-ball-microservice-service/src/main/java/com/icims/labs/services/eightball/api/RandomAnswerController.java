package com.icims.labs.services.eightball.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.icims.labs.services.eightball.service.RandomAnswerService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Random answers controller for integration testing.
 * 
 * @author imran.pasha
 */
@RestController
@RequestMapping("/api")
@Api(value = "Simple random answers resource")
public class RandomAnswerController {
	private static final Logger logger = LoggerFactory.getLogger(RandomAnswerController.class);

	@Autowired
	private RandomAnswerService randomAnsService;

	// Dummy service
	@GetMapping("/helloworld")
	@ApiOperation(value = "Dummy hello world service")
	public Map<String, String> helloWorld() {
		logger.info("Successfully received request for HelloWorld.");

		Map<String, String> helloWorld = new HashMap<>();
		helloWorld.put("pheonix", "Hello World from Phoenix!!!");

		return helloWorld;
	}
	
	@ApiOperation(value = "returns a random answer")
	@ApiResponses({
		@ApiResponse(code = 200, message = "OK")
	})
	@PostMapping("/answer")
	public Map<String, String> randomAnswer(@RequestBody String question) {
		logger.info("Fetching a random answer...");

		Map<String, String> answer = new HashMap<>();
		answer.put("answer", randomAnsService.getRandomAnswer());

		return answer;
	}
}
