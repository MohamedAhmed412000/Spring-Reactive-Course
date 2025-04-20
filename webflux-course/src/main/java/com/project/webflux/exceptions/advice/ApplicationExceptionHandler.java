package com.project.webflux.exceptions.advice;

import com.project.webflux.exceptions.CustomerNotFoundException;
import com.project.webflux.exceptions.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problemDetail.setType(URI.create("http://example.com/problems/customer-not-found"));
        problemDetail.setTitle("Customer Not Found");
        return problemDetail;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail handleInvalidInputException(InvalidInputException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("http://example.com/problems/invalid-input"));
        problemDetail.setTitle("Invalid Input");
        return problemDetail;
    }

}
