package com.project.customer.exceptions;

public class InsufficientSharesException extends RuntimeException {

    private static final String MESSAGE = "Customer [id=%s] doesn't have enough shares to complete transaction";

    public InsufficientSharesException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
