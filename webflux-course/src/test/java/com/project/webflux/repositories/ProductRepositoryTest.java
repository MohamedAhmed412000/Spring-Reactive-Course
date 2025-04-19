package com.project.webflux.repositories;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

@Slf4j
public class ProductRepositoryTest extends AbstractTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void findProductsBetween() {
        this.productRepository.findProductsByPriceBetween(1000, 2500)
            .doOnNext(product -> log.info("Product received: {}", product))
            .as(StepVerifier::create)
            .expectNextCount(3)
            .expectComplete()
            .verify();
    }

    @Test
    public void pageable() {
        this.productRepository.findAllBy(PageRequest.of(1, 3).withSort(
            Sort.by("price").ascending()))
            .doOnNext(product -> log.info("Product received: {}", product))
            .as(StepVerifier::create)
            .expectNextCount(3)
            .expectComplete()
            .verify();
    }

}
