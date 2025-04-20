package com.project.webflux.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer id) {
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingField(String fieldName) {
        return Mono.error(new InvalidInputException(fieldName));
    }

}
