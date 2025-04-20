package com.project.webflux.controllers;

import com.project.webflux.dto.CustomerDto;
import com.project.webflux.exceptions.ApplicationExceptions;
import com.project.webflux.services.CustomerService;
import com.project.webflux.validators.RequestValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    Flux<CustomerDto> getAllCustomers() {
        return this.customerService.getAllCustomers();
    }

    @GetMapping("/paginated")
    Flux<CustomerDto> getAllCustomersPaginated(
        @RequestParam(name = "page", defaultValue = "1") int page,
        @RequestParam(name = "size", defaultValue = "3") int size
        ) {
        return this.customerService.getAllCustomersPaginated(page, size);
    }

    @GetMapping("/{id}")
    Mono<CustomerDto> getCustomer(
        @PathVariable("id") Integer customerId
    ) {
        return this.customerService.getCustomer(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId));
    }

    @PostMapping
    Mono<CustomerDto> createCustomer(
        @RequestBody Mono<CustomerDto> customerDtoMono
    ) {
        return customerDtoMono.transform(RequestValidator.validateCustomer())
            .as(this.customerService::saveCustomer);
    }

    @PutMapping("{id}")
    Mono<CustomerDto> updateCustomer(
        @PathVariable("id") Integer customerId,
        @RequestBody Mono<CustomerDto> customerDtoMono
    ) {
        return customerDtoMono.transform(RequestValidator.validateCustomer())
            .as(validatedCustomerDtoMono -> this.customerService
                .updateCustomer(customerId, validatedCustomerDtoMono))
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId));
    }

    @DeleteMapping("{id}")
    Mono<Void> deleteCustomer(
        @PathVariable("id") Integer customerId
    ) {
        return this.customerService.deleteCustomer(customerId)
            .filter(isDeleted -> isDeleted)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
            .then();
    }


}
