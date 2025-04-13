package com.project.webflux.services;

import com.project.webflux.dto.CustomerDto;
import com.project.webflux.mappers.CustomerMapper;
import com.project.webflux.models.Customer;
import com.project.webflux.repositories.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Flux<CustomerDto> getAllCustomers() {
        return this.customerRepository.findAll()
            .map(customer -> CustomerMapper.convertToCustomerDto(customer, new CustomerDto()));
    }

    public Flux<CustomerDto> getAllCustomersPaginated(int page, int size) {
        return this.customerRepository.findAllBy(PageRequest.of(page - 1, size))
            .map(customer -> CustomerMapper.convertToCustomerDto(customer, new CustomerDto()));
    }


    public Mono<CustomerDto> getCustomer(Integer id) {
        return this.customerRepository.findById(id)
            .map(customer -> CustomerMapper.convertToCustomerDto(customer, new CustomerDto()));
    }

    public Mono<CustomerDto> saveCustomer(Mono<CustomerDto> customerDtoMono) {
        return customerDtoMono
            .map(customerDto -> CustomerMapper.convertToCustomer(customerDto, new Customer()))
            .flatMap(this.customerRepository::save)
            .map(customer -> CustomerMapper.convertToCustomerDto(customer, new CustomerDto()));
    }

    public Mono<CustomerDto> updateCustomer(Integer id, Mono<CustomerDto> customerDtoMono) {
        return this.customerRepository.findById(id)
            .flatMap(customer -> customerDtoMono)
            .map(customerDto -> CustomerMapper.convertToCustomer(customerDto, new Customer()))
            .doOnNext(customer -> customer.setId(id))
            .flatMap(this.customerRepository::save)
            .map(customer -> CustomerMapper.convertToCustomerDto(customer, new CustomerDto()));
    }

    public Mono<Boolean> deleteCustomer(Integer id) {
        return this.customerRepository.deleteCustomersById(id);
    }

}
