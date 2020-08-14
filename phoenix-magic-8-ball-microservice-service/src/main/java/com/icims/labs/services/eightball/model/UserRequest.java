package com.icims.labs.services.eightball.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRequest {
    String question;
    Language language;
    String userId;
}
