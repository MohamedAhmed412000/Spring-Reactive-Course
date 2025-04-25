package com.project.webflux.integration;

import com.project.webflux.dto.ProductDto;
import com.project.webflux.dto.UploadResponse;
import com.project.webflux.services.FileWriter;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.nio.file.Path;

public class ProductClientTest {

    private final WebClient client = getWebClient();
    private static final String PREMIUM_VALUE = "secret456";

    @Test
    public void testProductUpload() {
        Flux<ProductDto> productDtoFlux = Flux
//            .just(new ProductDto(null, "Phone", 1000))
            .range(1, 1_000_000).map(i -> new ProductDto(null, "Product-" + i, i*10))
//            .delayElements(Duration.ofSeconds(10))
            ;
        uploadProducts(productDtoFlux)
            .doOnNext(System.out::println)
            .as(StepVerifier::create)
            .expectNextCount(1)
            .verifyComplete();
    }

    @Test
    public void testProductDownload() {
        getAllProducts()
            .map(ProductDto::toString)
            .as(productStringFlux -> FileWriter.create(productStringFlux, Path.of("output.txt")))
            .as(StepVerifier::create)
            .expectComplete()
            .verify();
    }

    private Flux<ProductDto> getAllProducts() {
        return this.client.post()
            .uri("/products/download")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(ProductDto.class);
    }

    private Mono<UploadResponse> uploadProducts(Flux<ProductDto> productsFlux) {
        return this.client.post()
            .uri("/products/upload")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .contentType(MediaType.APPLICATION_NDJSON)
            .body(productsFlux, ProductDto.class)
            .retrieve()
            .bodyToMono(UploadResponse.class);
    }

    private WebClient getWebClient() {
        return WebClient.builder().baseUrl("http://localhost:8080").build();
    }

}
