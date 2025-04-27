package com.project.customer.exceptions.advice;

import com.project.customer.exceptions.CustomerNotFoundException;
import com.project.customer.exceptions.InsufficientBalanceException;
import com.project.customer.exceptions.InsufficientSharesException;
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

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("http://example.com/problems/insufficient-balance"));
        problemDetail.setTitle("Customer doesn't have sufficient balance for the transaction");
        return problemDetail;
    }

    @ExceptionHandler(InsufficientSharesException.class)
    public ProblemDetail handleInsufficientSharesException(InsufficientSharesException ex) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problemDetail.setType(URI.create("http://example.com/problems/insufficient-shares"));
        problemDetail.setTitle("Customer doesn't have sufficient shares for the transaction");
        return problemDetail;
    }

}
