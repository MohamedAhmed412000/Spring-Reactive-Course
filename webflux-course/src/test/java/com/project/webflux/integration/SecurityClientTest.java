package com.project.webflux.integration;

import com.project.webflux.dto.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Slf4j
@AutoConfigureWebTestClient
@SpringBootTest(properties = "logging.level.org.springframework.r2dbc=debug")
public class SecurityClientTest {

    private static final String WRONG_VALUE = "123";
    private static final String STANDARD_VALUE = "secret123";
    private static final String PREMIUM_VALUE = "secret456";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void unAuthorized() {
        // No token passed
        this.webTestClient.get()
            .uri("/customers")
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody()
            .isEmpty();

        // Wrong value passed
        CustomerDto customerDto = new CustomerDto(null, "Mohamed", "mohamed@example.com");
        this.webTestClient.post()
            .uri("/customers")
            .header(HttpHeaders.AUTHORIZATION, WRONG_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().isUnauthorized()
            .expectBody()
            .isEmpty();
    }

    @Test
    public void validTokenGet() {
        // Standard Permission
        this.webTestClient.get()
            .uri("/customers/2")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isEqualTo(2);

        // Premium Permission
        this.webTestClient.get()
            .uri("/customers/2")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isEqualTo(2);
    }

    @Test
    public void validTokenPostPutDelete() {
        CustomerDto customerDto = new CustomerDto(null, "Mohamed", "mohamed@example.com");

        // Post
        this.webTestClient.post()
            .uri("/customers")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isNotEmpty()
            .jsonPath("$.name").isEqualTo("Mohamed");

        // Put
        this.webTestClient.put()
            .uri("/customers/2")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isNotEmpty()
            .jsonPath("$.name").isEqualTo("Mohamed");

        // Delete
        this.webTestClient.delete()
            .uri("/customers/2")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .isEmpty();
    }

    @Test
    public void invalidTokenPostPutDelete() {
        CustomerDto customerDto = new CustomerDto(null, "Mohamed", "mohamed@example.com");

        // Post
        this.webTestClient.post()
            .uri("/customers")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().isForbidden()
            .expectBody()
            .isEmpty();

        // Put
        this.webTestClient.put()
            .uri("/customers/2")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().isForbidden()
            .expectBody()
            .isEmpty();

        // Delete
        this.webTestClient.delete()
            .uri("/customers/2")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .exchange()
            .expectStatus().isForbidden()
            .expectBody()
            .isEmpty();
    }


}
