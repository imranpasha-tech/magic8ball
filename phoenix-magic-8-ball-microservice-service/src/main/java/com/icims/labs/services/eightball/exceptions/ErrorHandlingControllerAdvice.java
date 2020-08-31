package com.icims.labs.services.eightball.exceptions;

import com.icims.labs.services.eightball.enums.Answers;
import com.icims.labs.services.eightball.model.SentimentAnswer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    SentimentAnswer onConstraintValidationException(ConstraintViolation e) {
        return SentimentAnswer.builder().answer(Answers.getAnswerByValue(21)).build();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    SentimentAnswer onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return SentimentAnswer.builder().answer(Answers.getAnswerByValue(21)).build();
    }
}
