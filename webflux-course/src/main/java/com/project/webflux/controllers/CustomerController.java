package com.project.webflux.controllers;

import com.project.webflux.dto.CustomerDto;
import com.project.webflux.services.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    Mono<ResponseEntity<CustomerDto>> getCustomer(
        @PathVariable("id") Integer customerId
    ) {
        return this.customerService.getCustomer(customerId)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    Mono<CustomerDto> createCustomer(
        @RequestBody Mono<CustomerDto> customerDtoMono
    ) {
        return this.customerService.saveCustomer(customerDtoMono);
    }

    @PutMapping("{id}")
    Mono<ResponseEntity<CustomerDto>> updateCustomer(
        @PathVariable("id") Integer customerId,
        @RequestBody Mono<CustomerDto> customerDtoMono
    ) {
        return this.customerService.updateCustomer(customerId, customerDtoMono)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    Mono<ResponseEntity<Void>> deleteCustomer(
        @PathVariable("id") Integer customerId
    ) {
        return this.customerService.deleteCustomer(customerId)
            .filter(isDeleted -> isDeleted)
            .map(isDeleted -> ResponseEntity.ok().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }


}
