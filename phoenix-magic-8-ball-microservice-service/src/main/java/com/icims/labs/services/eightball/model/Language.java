package com.icims.labs.services.eightball.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Language {
    String locale;
    String code;
    String name;
}
