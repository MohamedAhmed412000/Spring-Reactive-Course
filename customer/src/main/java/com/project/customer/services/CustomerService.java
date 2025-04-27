package com.project.customer.services;

import com.project.customer.domain.models.Customer;
import com.project.customer.dto.CustomerInformation;
import com.project.customer.exceptions.ApplicationExceptions;
import com.project.customer.mapppers.EntityDtoMapper;
import com.project.customer.repositories.CustomerRepository;
import com.project.customer.repositories.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.customerRepository.findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
            .flatMap(this::buildCustomerInformation);
    }

    private Mono<CustomerInformation> buildCustomerInformation(Customer customer) {
        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
            .collectList()
            .map(portfolioItems -> EntityDtoMapper.mapToCustomerDto(customer, portfolioItems));
    }

}
