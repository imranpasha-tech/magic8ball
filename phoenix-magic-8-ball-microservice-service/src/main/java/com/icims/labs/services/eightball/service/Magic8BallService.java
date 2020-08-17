package com.icims.labs.services.eightball.service;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.dto.Answers;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class Magic8BallService {
    private static final Logger logger = LoggerFactory.getLogger(Magic8BallService.class);

    @Autowired
    private Magic8BallRepository magic8BallRepository;

    /**
     * Service method that selects a random mocked answer.
     *
     * @return String
     */
    public String getRandomAnswer(UserRequest userRequest) {

        String randomAnswer = Arrays.asList(Answers.values()).get((new Random()).nextInt(20)).getAnswerKey();
        logger.info("Random answer is fetched: {}", randomAnswer);
        magic8BallRepository.save(buildQuestionHistory(userRequest));
        return randomAnswer;
    }

    private History buildQuestionHistory(UserRequest userRequest) {
        return History.builder().question(userRequest.getQuestion())
                .frequency(1)
                .languageCode(userRequest.getLanguage().getCode())
                .createdDate(LocalDateTime.now())
                .build();

    }

    public List<History> getHistory() {
        return magic8BallRepository.findAll();
    }
}
