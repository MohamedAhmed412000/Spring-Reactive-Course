package com.project.webflux.integration;

import com.project.webflux.dto.ProductDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

@Slf4j
@AutoConfigureWebTestClient
@SpringBootTest(properties = "logging.level.org.springframework.r2dbc=debug")
public class ServerSentEventsTest {

    @Autowired
    private WebTestClient client;

    @Test
    public void testServerSentEvents() {
        this.client.get()
            .uri("/products/stream/500")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(ProductDto.class)
            .getResponseBody()
            .take(3)
            .doOnNext(productDto -> log.info("Received product: {}", productDto))
            .collectList()
            .as(StepVerifier::create)
            .assertNext(productDtos -> {
                Assertions.assertEquals(3, productDtos.size());
                Assertions.assertTrue(productDtos.stream().allMatch(productDto ->
                    productDto.price() <= 500));
            })
            .expectComplete()
            .verify();
    }

}
