package com.icims.labs.services.eightball.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;
import com.amazonaws.services.comprehend.model.DetectSentimentRequest;
import com.amazonaws.services.comprehend.model.DetectSentimentResult;
import com.icims.labs.services.eightball.model.SentimentResult;
import com.icims.labs.services.eightball.model.UserRequest;
import com.icims.labs.services.eightball.service.ComprehendService;


/**
 * This service contacts AWS comprehend service and gives sentiment results.
 * 
 * @author imran.pasha {@literal ipasha.icims.com}
 *
 */
@Service
public class ComprehendServiceImpl implements ComprehendService {
	private static final Logger logger = LoggerFactory.getLogger(ComprehendServiceImpl.class);
	private AmazonComprehend comprehend;
	AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();

	public ComprehendServiceImpl() {
		this.comprehend = AmazonComprehendClientBuilder.standard().withCredentials(awsCreds).withRegion("us-east-1")
				.build();
	}

	@Override
	public SentimentResult getQuestionSentiment(UserRequest userRequest) {
		logger.info("Calling DetectSentiment");
		
		if (userRequest != null && userRequest.getQuestion() != null) {
			DetectSentimentRequest detectSentimentRequest = new DetectSentimentRequest()
					.withText(userRequest.getQuestion()).withLanguageCode(userRequest.getLanguage().getCode());
			DetectSentimentResult detectSentimentResult = comprehend.detectSentiment(detectSentimentRequest);
			logger.info("Detected sentiment is: {} ", detectSentimentResult.getSentiment());

			SentimentResult sentimentResult = SentimentResult.builder().score(detectSentimentResult.getSentimentScore())
					.sentiment(detectSentimentResult.getSentiment()).build();
			
			return sentimentResult;
		} else 
			throw new NullPointerException();
		
	}

}
