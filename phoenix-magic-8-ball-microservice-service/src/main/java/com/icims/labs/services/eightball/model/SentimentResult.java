package com.icims.labs.services.eightball.model;

import com.amazonaws.services.comprehend.model.SentimentScore;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SentimentResult {
	private SentimentScore score;
	private String sentiment;
}
