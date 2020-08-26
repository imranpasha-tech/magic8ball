package com.icims.labs.services.eightball.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionDTO {

    String question;

    String truncatedQuestion;

    String answer;

    String languageCode;
}
