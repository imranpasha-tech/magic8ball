package com.icims.labs.services.eightball.service.impl;

import com.icims.labs.services.eightball.commons.Magic8BallCommons;
import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.QuestionDTO;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.Magic8BallService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.Optional;
import java.util.Random;

@Service
public class Magic8BallServiceImpl implements Magic8BallService {
    private static final Logger logger = LoggerFactory.getLogger(Magic8BallServiceImpl.class);

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
        QuestionDTO questionDTO = Magic8BallCommons.buildQuestionDTO(userRequest, randomAnswer);
        saveQuestionHistory(questionDTO);
        return randomAnswer;
    }

    @Transactional
    private void saveQuestionHistory(QuestionDTO questionDTO) {
        Optional<History> history = magic8BallRepository.findByTruncatedQuestion(questionDTO.getTruncatedQuestion(), questionDTO.getLanguageCode());
        if(history.isPresent()){
            int frequency = history.get().getFrequency();
            history.get().setFrequency(++frequency);
            magic8BallRepository.save(history.get());
        }
        else {
            magic8BallRepository.save(Magic8BallCommons.buildQuestionHistory(questionDTO, 1));
        }
    }
}
