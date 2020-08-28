package com.icims.labs.services.eightball.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
	@NotEmpty(message = "Please provide a question")
	String question;
	@NotNull(message = "Please provide a question")
	Language language;
	@NotEmpty(message = "Please provide a userId")
	String userId;
}
