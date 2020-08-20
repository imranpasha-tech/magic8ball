package com.icims.labs.services.eightball.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponse {

    String answer;
    String answerType;
    String question;
    boolean isTrending;
    int questionFrequency;
}
