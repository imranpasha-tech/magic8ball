package com.icims.labs.services.eightball.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SentimentAnswer {
	private String answer;
	private SentimentResult sentimentResult;
}
