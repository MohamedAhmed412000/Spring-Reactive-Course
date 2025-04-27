package com.project.customer.exceptions;

public class InsufficientBalanceException extends RuntimeException {

    private static final String MESSAGE = "Customer [id=%s] doesn't have sufficient balance to complete transaction";

    public InsufficientBalanceException(Integer id) {
        super(MESSAGE.formatted(id));
    }
}
