package com.project.webflux.reactive.controller;

import com.project.webflux.reactive.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/reactive")
@RequiredArgsConstructor
@Slf4j
public class ReactiveController {

    private final WebClient webClient;

    @GetMapping("/products")
    Flux<ProductDto> getProducts() {
        return this.webClient.get()
            .uri("/demo01/products")
            .retrieve()
            .bodyToFlux(ProductDto.class)
            .doOnNext(productDto -> log.info("received: {}", productDto));
    }

    @GetMapping(value = "/products/fail", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<ProductDto> getProductsFailing() {
        return this.webClient.get()
            .uri("/demo01/products/notorious")
            .retrieve()
            .bodyToFlux(ProductDto.class)
            .onErrorComplete()
            .doOnNext(productDto -> log.info("received: {}", productDto));
    }

    @GetMapping(value = "/products/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<ProductDto> getProductsStream() {
        return this.webClient.get()
            .uri("/demo01/products")
            .retrieve()
            .bodyToFlux(ProductDto.class)
            .doOnNext(productDto -> log.info("received: {}", productDto));
    }

}
