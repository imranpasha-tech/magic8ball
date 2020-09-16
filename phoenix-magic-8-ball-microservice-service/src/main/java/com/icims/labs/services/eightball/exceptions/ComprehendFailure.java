package com.icims.labs.services.eightball.exceptions;

public class ComprehendFailure extends RuntimeException {
    public ComprehendFailure(String exception) {
        super(exception);
    }
}
