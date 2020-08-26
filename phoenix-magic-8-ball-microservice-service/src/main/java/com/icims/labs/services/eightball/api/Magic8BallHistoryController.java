package com.icims.labs.services.eightball.api;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.service.Magic8BallHistoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(value = "Simple random answers resource")
public class Magic8BallHistoryController {

    @Autowired
    private Magic8BallHistoryService magic8BallHistoryService;

    private static final Logger logger = LoggerFactory.getLogger(Magic8BallHistoryController.class);

    @ApiOperation(value = "returns history of user")
    @GetMapping("/history")
    public List<History> getHistory() {
        return magic8BallHistoryService.getHistory();
    }

    @ApiOperation(value = "returns trending questions")
    @GetMapping("/trendingQuestions")
    public List<History> getTrendingQuestions(@RequestParam String languageCode) {
        return magic8BallHistoryService.getTrendingQuestions(languageCode);
    }
}
