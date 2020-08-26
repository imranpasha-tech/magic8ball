package com.icims.labs.services.eightball.commons;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.QuestionDTO;
import com.icims.labs.services.eightball.model.UserRequest;

import java.time.LocalDateTime;

public class Magic8BallCommons {

    public static String truncate(String question) {
        return question.replaceAll("[,;\\s]", "");
    }

    public static History buildQuestionHistory(QuestionDTO questionDTO, int frequency) {
        return History.builder().question(questionDTO.getQuestion()).truncatedQuestion(questionDTO.getTruncatedQuestion())
                .frequency(frequency)
                .languageCode(questionDTO.getLanguageCode())
                .createdDate(LocalDateTime.now()).answer(questionDTO.getAnswer())
                .build();
    }

    public static QuestionDTO buildQuestionDTO(UserRequest userRequest, String randomAnswer) {
        String truncatedQuestion = truncate(userRequest.getQuestion()).toLowerCase();
        return QuestionDTO.builder().question(userRequest.getQuestion()).truncatedQuestion(truncatedQuestion).languageCode(userRequest.getLanguage().getCode()).answer(randomAnswer).build();
    }
}
