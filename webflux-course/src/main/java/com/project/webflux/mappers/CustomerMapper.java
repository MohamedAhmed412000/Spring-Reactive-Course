package com.project.webflux.mappers;

import com.project.webflux.dto.CustomerDto;
import com.project.webflux.models.Customer;

public class CustomerMapper {

    public static Customer convertToCustomer(CustomerDto customerDto, Customer customer) {
        customer.setId(customerDto.getId());
        customer.setName(customerDto.getName());
        customer.setEmail(customerDto.getEmail());
        return customer;
    }

    public static CustomerDto convertToCustomerDto(Customer customer, CustomerDto customerDto) {
        return CustomerDto.builder()
            .id(customer.getId())
            .name(customer.getName())
            .email(customer.getEmail())
            .build();
    }

}
