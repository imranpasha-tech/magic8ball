package com.icims.labs.services.eightball.util;

import com.icims.labs.services.eightball.entity.History;
import com.icims.labs.services.eightball.model.Language;
import com.icims.labs.services.eightball.model.QuestionDTO;
import com.icims.labs.services.eightball.model.UserRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TestUtils {

    public static List<History> buildHistory() {
        List<History> history = new ArrayList<>();
        history.add(History.builder().question("Will it rain?").languageCode("en-US").createdDate(LocalDateTime.now()).frequency(3).build());
        return history;
    }

    public static UserRequest buildMockUserRequest() {
        Language language = Language.builder().code("en_US").locale("en_US").name("USA").build();
        return UserRequest.builder().question("Will it rain ?").userId("").language(language).build();
    }


    public static History buildQuestionHistory(UserRequest userRequest) {
        return History.builder().question(userRequest.getQuestion())
                .frequency(1).truncatedQuestion("willitrain?")
                .languageCode(userRequest.getLanguage().getCode())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
