package com.icims.labs.services.eightball.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.SentimentAnswer;
import com.icims.labs.services.eightball.model.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /*
     * validate ? on request validate request lenght max 120 characters if request
     * has only ? ,response should be ! if request has upper/lower/mix ,store all
     * them in lower case for "trending/history"
     *
     *
     */
    @ApiOperation(value = "returns a random answer")
    @ApiResponses({ @ApiResponse(code = 200, message = "OK") })
    @PostMapping("/answer")
    public SentimentAnswer getRandomAnswer(@Valid @RequestBody UserRequest userRequest) {
        logger.info("Fetching a random answer...");

        Map<String, String> answer = new HashMap<>();
        String responseKey="answer";
        try {
            if (userRequest != null) {
                String question = userRequest.getQuestion();
                if (question.trim().length() == 1 && question.trim().contentEquals("?")) {
                    answer.put(responseKey, "!");
                   // return answer;
                }
                if (question.endsWith("?") && question.length() <= 120) {
                   // answer.put(responseKey, magic8BallService.getRandomAnswer(userRequest));
                    return magic8BallService.getRandomAnswer(userRequest);
                } else {
                    answer.put(responseKey, Answers.getAnswerByValue(21));
                    //return answer;
                }
            }

        } catch (Exception e) {
            logger.error("Exception is raised during /api/answer api processing ", e);
        }
        return magic8BallService.getRandomAnswer(userRequest);

    }
    @ApiOperation(value = "returns history of user")
    @GetMapping("/history")
    public List<History> getHistory() {
        return magic8BallService.getHistory();
    }

    @ApiOperation(value = "returns trending questions")
    @GetMapping("/trendingQuestions")
    public List<History> getTrendingQuestions(@RequestParam String languageCode) {
        return magic8BallService.getTrendingQuestions(languageCode);
    }


}
