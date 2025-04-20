package com.project.webflux.exceptions;

public class InvalidInputException extends RuntimeException {

    private static final String MESSAGE = "Field [name=%s] is required";

    public InvalidInputException(String fieldName) {
        super(MESSAGE.formatted(fieldName));
    }
}
