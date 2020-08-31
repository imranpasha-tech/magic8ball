package com.icims.labs.services.eightball.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
	@NotEmpty(message = "Please provide a question")
	@Pattern(regexp = "[^.!?]+\\?")
	@Size(min = 5, max = 120)
	String question;
	@NotNull(message = "Please provide a question")
	Language language;
	@NotEmpty(message = "Please provide a userId")
	String userId;
}
