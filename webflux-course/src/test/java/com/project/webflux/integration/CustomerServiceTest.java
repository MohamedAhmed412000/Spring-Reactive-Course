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
public class CustomerServiceTest {

    private static final String STANDARD_VALUE = "secret123";
    private static final String PREMIUM_VALUE = "secret456";

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void getAllCustomers() {
        this.webTestClient.get()
            .uri("/customers")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBodyList(CustomerDto.class)
            .value(list -> log.info("Response: {}", list))
            .hasSize(10);
    }

    @Test
    public void getPaginatedCustomers() {
        this.webTestClient.get()
            .uri("/customers/paginated")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.length()").isEqualTo(3)
            .jsonPath("$[0].id").isEqualTo(1)
            .jsonPath("$[1].id").isEqualTo(2);
    }

    @Test
    public void getCustomerById() {
        this.webTestClient.get()
            .uri("/customers/1")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("sam")
            .jsonPath("$.email").isEqualTo("sam@gmail.com");
    }

    @Test
    public void createAndDeleteCustomer() {
        CustomerDto customerDto = new CustomerDto(null, "mohamed", "mohamed@example.com");
        this.webTestClient.post()
            .uri("/customers")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isEqualTo(11)
            .jsonPath("$.name").isEqualTo("mohamed")
            .jsonPath("$.email").isEqualTo("mohamed@example.com");

        this.webTestClient.delete()
            .uri("/customers/11")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectBody().isEmpty();
    }

    @Test
    public void updateCustomer() {
        CustomerDto customerDto = new CustomerDto(null, "mohamed", "mohamed@example.com");
        this.webTestClient.put()
            .uri("/customers/10")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .expectHeader().contentType(MediaType.APPLICATION_JSON_VALUE)
            .expectBody()
            .consumeWith(response ->
                log.info("Response: {}", new String(Objects.requireNonNull(response.getResponseBody()))))
            .jsonPath("$.id").isEqualTo(10)
            .jsonPath("$.name").isEqualTo("mohamed")
            .jsonPath("$.email").isEqualTo("mohamed@example.com");
    }

    @Test
    public void InvalidRequest() {
        CustomerDto customerDto = new CustomerDto(null, null, "mohamed");
        this.webTestClient.post()
            .uri("/customers")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().isBadRequest()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.status").isEqualTo(400)
            .jsonPath("$.title").isEqualTo("Invalid Input");
    }

    @Test
    public void customerNotFound() {
        // get
        this.webTestClient.get()
            .uri("/customers/20")
            .header(HttpHeaders.AUTHORIZATION, STANDARD_VALUE)
            .exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.status").isEqualTo(404)
            .jsonPath("$.title").isEqualTo("Customer Not Found");

        // delete
        this.webTestClient.delete()
            .uri("/customers/20")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.status").isEqualTo(404)
            .jsonPath("$.title").isEqualTo("Customer Not Found");

        // update
        CustomerDto customerDto = new CustomerDto(null, "mohamed", "mohamed@example.com");
        this.webTestClient.put()
            .uri("/customers/20")
            .header(HttpHeaders.AUTHORIZATION, PREMIUM_VALUE)
            .bodyValue(customerDto)
            .exchange()
            .expectStatus().isNotFound()
            .expectHeader().contentType(MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .jsonPath("$.status").isEqualTo(404)
            .jsonPath("$.title").isEqualTo("Customer Not Found");
    }

}

