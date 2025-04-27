package com.project.gateway.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer customerId) {
        return Mono.error(new CustomerNotFoundException(customerId));
    }

    public static <T> Mono<T> invalidTrade(String message) {
        return Mono.error(new InvalidTradeRequestException(message));
    }

}
