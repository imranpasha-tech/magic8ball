package com.icims.labs.services.eightball.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icims.labs.services.eightball.dto.Answers;
import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icims.labs.services.eightball.service.Magic8BallService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;

/**
 * Random answers controller for integration testing.
 * 
 * @author imran.pasha
 */
@RestController
@RequestMapping("/api")
@Api(value = "Simple random answers resource")
public class Magic8BallController {
	private static final Logger logger = LoggerFactory.getLogger(Magic8BallController.class);

	@Autowired
	private Magic8BallService magic8BallService;

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
	public Map<String, String> getRandomAnswer(@Valid @RequestBody UserRequest userRequest) {
		logger.info("Fetching a random answer...");
		/*
		 * validate ? on request 
		 *validate request lenght max 120 characters
		 *if request has only ? ,response should be ! 
		 *if request has upper/lower/mix ,store all them in lower case for "trending/history"
		 *
		 *
		 */
		Map<String, String> answer = new HashMap<>();
		try
		{
			if(userRequest != null)
			{
				String question=userRequest.getQuestion();
				if(question.trim().length()==1 && question.trim().contentEquals("?"))
				{
					answer.put("answer","!");
					return answer;
				}
				if(question.endsWith("?") && question.length()<=120)
				{
					answer.put("answer", magic8BallService.getRandomAnswer(userRequest));
					return answer;	
				}
				else {
					answer.put("answer", Answers.getAnswerByValue(21));
					return answer;
				}
			}
			
		}
		catch(Exception e)
		{
			logger.error("Exception is raised during /api/answer api processing ", e);
		}
		return answer;
		
	}
	@ApiOperation(value = "returns history of user")
	@GetMapping("/history")
	public List<History> getHistory() {
		return magic8BallService.getHistory();
	}
}
