package com.risi.mvc.data.demo.rest;

import com.risi.mvc.data.demo.exception.CustomerNotFoundException;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice // AOP
public class RestExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(Exception e) {
        RestErrorResponse error = new RestErrorResponse();
        error.setStatus(HttpStatus.BAD_REQUEST.value());
        error.setMessage("Something went wrong!");
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<RestErrorResponse> handleException(CustomerNotFoundException exc) {
        RestErrorResponse error = new RestErrorResponse();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setMessage(exc.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @Data
    private static class RestErrorResponse {
        private int status;
        private String message;
        private LocalDateTime dateTime = LocalDateTime.now();
    }
}
