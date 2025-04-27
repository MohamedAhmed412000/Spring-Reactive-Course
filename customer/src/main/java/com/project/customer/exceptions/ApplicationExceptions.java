package com.project.customer.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer customerId) {
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static <T> Mono<T> InsufficientBalance(Integer customerId) {
        return Mono.error(new InsufficientBalanceException(customerId));
    }

    public static <T> Mono<T> InsufficientShares(Integer customerId) {
        return Mono.error(new InsufficientSharesException(customerId));
    }

}
