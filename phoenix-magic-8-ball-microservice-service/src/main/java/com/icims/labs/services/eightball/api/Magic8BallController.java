package com.icims.labs.services.eightball.api;

import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.SentimentAnswer;
import com.icims.labs.services.eightball.model.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import com.icims.labs.services.eightball.service.Magic8BallService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    /*
     * Method performs:
     * request validation when
     * 	1. Question is "?" then returns "!"
     *  2. Question is "*?" and length is <= 120 characters then returns proper response
     *  3. else returns "try_later"
     *
     */
	@ApiOperation(value = "returns an answer with sentiment results and score for a given question")
	@ApiResponses({ @ApiResponse(code = 200, message = "OK") })
	@PostMapping("/answer")
	public SentimentAnswer getRandomAnswer(@Valid @RequestBody UserRequest userRequest) {
		logger.info("Fetching a random answer...");

		try {
			return magic8BallService.getRandomAnswer(userRequest);
		} catch (Exception e) {
			logger.error("Exception is raised during /api/answer api processing ", e);
			return SentimentAnswer.builder().answer(Answers.getAnswerByValue(21)).build();
		}

	}
}