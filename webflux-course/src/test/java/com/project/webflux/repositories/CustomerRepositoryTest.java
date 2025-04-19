package com.project.webflux.repositories;

import com.project.webflux.models.Customer;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@Slf4j
public class CustomerRepositoryTest extends AbstractTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void findAll() {
        this.customerRepository.findAll()
            .doOnNext(customer -> log.info("Received customer: {}", customer))
            .as(StepVerifier::create)
            .expectNextCount(10)
            .expectComplete()
            .verify();
    }

    @Test
    public void findById() {
        this.customerRepository.findById(1)
            .doOnNext(customer -> log.info("Received customer: {}", customer))
            .as(StepVerifier::create)
            .assertNext(customer -> Assertions.assertEquals("sam", customer.getName()))
            .expectComplete()
            .verify();
    }

    @Test
    public void findByEmail() {
        this.customerRepository.findByEmailEndingWith("ke@gmail.com")
            .doOnNext(customer -> log.info("Received customer: {}", customer))
            .as(StepVerifier::create)
            .expectNextCount(2)
            .expectComplete()
            .verify();
    }

    @Test
    public void insertAndDeleteCustomer() {
        Customer newCustomer = new Customer(null, "mohamed", "mohamed@example.com");
        this.customerRepository.save(newCustomer)
            .doOnNext(customer -> log.info("Received customer: {}", customer))
            .as(StepVerifier::create)
            .assertNext(customer -> Assertions.assertNotNull(customer.getId()))
            .expectComplete()
            .verify();
        this.customerRepository.count()
            .as(StepVerifier::create)
            .expectNextCount(1L)
            .verifyComplete();
        this.customerRepository.deleteById(11)
            .then(this.customerRepository.count())
            .as(StepVerifier::create)
            .expectNextCount(1L)
            .expectComplete()
            .verify();
    }

    @Test
    public void updateCustomer() {
        this.customerRepository.findById(10)
            .doOnNext(customer -> customer.setName("Mohamed"))
            .flatMap(customer -> customerRepository.save(customer))
            .doOnNext(customer -> log.info("Received customer: {}", customer))
            .as(StepVerifier::create)
            .assertNext(customer -> Assertions.assertEquals("Mohamed", customer.getName()))
            .expectComplete()
            .verify();
    }


}
