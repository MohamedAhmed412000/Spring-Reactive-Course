package com.project.gateway;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Slf4j
public class CustomerInformationTest extends AbstractIntegrationTest {

    @Test
    public void testCustomerInformation() {
        // Mock customer service first
        mockCustomerService("customer-service/customer-information-200.json", 200);
        getCustomerInformation(HttpStatus.OK)
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("Ahmed")
            .jsonPath("$.balance").isEqualTo(10000)
            .jsonPath("$.holdings").isNotEmpty();
    }
    
    @Test
    public void testCustomerNotFound() {
        // Mock customer service first
        mockCustomerService("customer-service/customer-information-404.json", 404);
        getCustomerInformation(HttpStatus.NOT_FOUND)
            .jsonPath("$.detail").isEqualTo("Customer [id=1] is not found")
            .jsonPath("$.title").isNotEmpty();
    }
    
    private void mockCustomerService(String path, int statusCode) {
        String responseBody = resourceToString(path);
        mockServerClient
            .when(HttpRequest.request().withPath("/customers/1"))
            .respond(HttpResponse.response(responseBody)
                .withStatusCode(statusCode)
                .withContentType(MediaType.APPLICATION_JSON)
            );
    }
    
    private WebTestClient.BodyContentSpec getCustomerInformation(HttpStatus status) {
        return this.webTestClient.get()
            .uri("/customers/{customer-id}", 1)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectHeader().contentType((status == HttpStatus.OK)?
                org.springframework.http.MediaType.APPLICATION_JSON:
                org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .consumeWith(response -> log.info("Response: {}",
                new String(Objects.requireNonNull(response.getResponseBody()))));
    }

}
