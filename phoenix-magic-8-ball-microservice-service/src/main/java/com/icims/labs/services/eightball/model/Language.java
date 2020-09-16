package com.icims.labs.services.eightball.model;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Data
@Builder
public class Language {
    String locale;
    String code;
    String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Language language = (Language) o;
        return locale.equals(language.locale) &&
                code.equals(language.code) &&
                name.equals(language.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
