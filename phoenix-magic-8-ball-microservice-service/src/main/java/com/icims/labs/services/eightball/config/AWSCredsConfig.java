package com.icims.labs.services.eightball.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.comprehend.AmazonComprehend;
import com.amazonaws.services.comprehend.AmazonComprehendClientBuilder;

@Configuration
public class AWSCredsConfig {
	private AWSCredentialsProvider awsCreds = DefaultAWSCredentialsProviderChain.getInstance();
	
	@Bean
	public AmazonComprehend getAwsCreds() {
		return AmazonComprehendClientBuilder.standard().withCredentials(awsCreds).withRegion("us-east-1")
				.build();
	}
}
