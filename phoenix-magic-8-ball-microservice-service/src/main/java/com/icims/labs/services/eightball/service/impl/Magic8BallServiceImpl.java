package com.icims.labs.services.eightball.service.impl;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.QuestionDTO;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.repository.Magic8BallRepository;
import com.icims.labs.services.eightball.service.Magic8BallService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
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
        QuestionDTO questionDTO = buildQuestionDTO(userRequest, randomAnswer);
        saveQuestionHistory(questionDTO);
        return randomAnswer;
    }


    private QuestionDTO buildQuestionDTO(UserRequest userRequest, String randomAnswer) {
        String truncatedQuestion = truncate(userRequest.getQuestion()).toLowerCase();
        return QuestionDTO.builder().question(userRequest.getQuestion()).truncatedQuestion(truncatedQuestion).languageCode(userRequest.getLanguage().getCode()).answer(randomAnswer).build();
    }

    private void saveQuestionHistory(QuestionDTO questionDTO) {
        Optional<History> history = magic8BallRepository.findByTruncatedQuestion(questionDTO.getTruncatedQuestion(), questionDTO.getLanguageCode());
        if(history.isPresent()){
            int frequency = history.get().getFrequency();
            history.get().setFrequency(++frequency);
            magic8BallRepository.save(history.get());
        }
        else {
            magic8BallRepository.save(buildQuestionHistory(questionDTO, 1));
        }
    }

    private String truncate(String question) {
        return question.replaceAll("[,;\\s]", "");
    }

    private History buildQuestionHistory(QuestionDTO questionDTO, int frequency) {
        return History.builder().question(questionDTO.getQuestion()).truncatedQuestion(questionDTO.getTruncatedQuestion())
                .frequency(frequency)
                .languageCode(questionDTO.getLanguageCode())
                .createdDate(LocalDateTime.now()).answer(questionDTO.getAnswer())
                .build();

    }

    public List<History> getHistory() {
        return magic8BallRepository.findAll();
    }

    @Override
    public List<History> getTrendingQuestions(String languageCode) {
        return magic8BallRepository.getTrendingQuestionsByLanguage(languageCode, PageRequest.of(0, 25));
    }


}
