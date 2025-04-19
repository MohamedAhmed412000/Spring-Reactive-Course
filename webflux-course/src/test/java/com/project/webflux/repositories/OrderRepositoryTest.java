package com.project.webflux.repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

@Slf4j
public class OrderRepositoryTest extends AbstractTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void productsOrderedByCustomer() {
        this.orderRepository.findAllProductsOrderedByCustomerName("sam")
            .doOnNext(product -> log.info("Product received: {}", product))
            .as(StepVerifier::create)
            .expectNextCount(2)
            .expectComplete()
            .verify();
    }

    @Test
    public void orderDetailsByProductName() {
        this.orderRepository.findAllOrdersByProductDesc("iphone 18")
            .doOnNext(orderDetails -> log.info("Order received: {}", orderDetails))
            .as(StepVerifier::create)
            .expectNextCount(3)
            .expectComplete()
            .verify();
    }

}
