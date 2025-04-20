package com.project.webflux.validators;

import com.project.webflux.dto.CustomerDto;
import com.project.webflux.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDto>> validateCustomer() {
        return customerDtoMono -> customerDtoMono
            .filter(hasName())
            .switchIfEmpty(ApplicationExceptions.missingField("name"))
            .filter(hasValidEmail())
            .switchIfEmpty(ApplicationExceptions.missingField("email"));
    }

    private static Predicate<CustomerDto> hasName() {
        return customerDto -> Objects.nonNull(customerDto.getName());
    }

    private static Predicate<CustomerDto> hasValidEmail() {
        return customerDto -> Objects.nonNull(customerDto.getEmail()) &&
            customerDto.getEmail().matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }

}
