package com.project.webflux.services;

import com.project.webflux.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;

public class ReactiveCallsTest extends AbstractWebClient {

    private final WebClient webClient = createWebClient();

    @Test
    public void testMono() throws InterruptedException {
        this.webClient.get()
            .uri("/lec01/product/1")
            .retrieve()
            .bodyToMono(ProductDto.class)
            .subscribe(System.out::println);

        Thread.sleep(2000);
    }

    @Test
    public void testMonoConcurrentRequests() throws InterruptedException {
        for (int i = 0; i < 100; i++) {
            this.webClient.get()
                .uri("/lec01/product/{id}", i)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .subscribe(System.out::println);
        }

        Thread.sleep(2000);
    }

    @Test
    public void testFluxRequest() {
        this.webClient.get()
            .uri("/lec02/product/stream")
            .retrieve()
            .bodyToFlux(ProductDto.class)
            .take(3)
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .expectComplete()
            .verify();
    }

    @Test
    public void testPostRequestWithBodyValue() {
        ProductDto productDto = new ProductDto(null, "Phone", 1000);
        this.webClient.post()
            .uri("/lec03/product")
            .bodyValue(productDto)
            .retrieve()
            .bodyToMono(ProductDto.class)
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .expectComplete()
            .verify();
    }


    @Test
    public void testPostRequestWithBody() {
        Mono<ProductDto> productDtoMono = Mono.just(new ProductDto(null, "Phone", 1000))
            .delayElement(Duration.ofSeconds(1));
        this.webClient.post()
            .uri("/lec03/product")
            .body(productDtoMono, ProductDto.class)
            .retrieve()
            .bodyToMono(ProductDto.class)
            .doOnNext(System.out::println)
            .then()
            .as(StepVerifier::create)
            .expectComplete()
            .verify();
    }


}
