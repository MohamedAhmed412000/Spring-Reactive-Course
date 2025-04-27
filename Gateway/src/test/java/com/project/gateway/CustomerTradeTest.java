package com.project.gateway;

import com.project.gateway.domain.enums.TickerEnum;
import com.project.gateway.domain.enums.TradeActionEnum;
import com.project.gateway.dto.TradeRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.RegexBody;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Slf4j
public class CustomerTradeTest extends AbstractIntegrationTest {

    @Test
    public void successTrade() {
        // Mock stock service price response
        mockStockService("stock-service/stock-price-200.json", 200);

        // Mock customer service trade response
        mockCustomerService("customer-service/customer-trade-200.json", 200);

        TradeRequest request = new TradeRequest(TickerEnum.GOOGLE, 2, TradeActionEnum.BUY);
        sendTrade(request, HttpStatus.OK)
            .jsonPath("$.customerId").isEqualTo(1)
            .jsonPath("$.action").isEqualTo(TradeActionEnum.BUY)
            .jsonPath("$.price").isEqualTo(150);
    }

    @Test
    public void failedTrade() {
        // Mock stock service price response
        mockStockService("stock-service/stock-price-200.json", 200);

        // Mock customer service trade response
        mockCustomerService("customer-service/customer-trade-403.json", 400);

        TradeRequest request = new TradeRequest(TickerEnum.GOOGLE, 2, TradeActionEnum.BUY);
        sendTrade(request, HttpStatus.BAD_REQUEST)
            .jsonPath("$.detail").isEqualTo("Customer [id=1] doesn't have sufficient" +
                " balance to complete transaction");
    }

    @Test
    public void inputValidation() {
        TradeRequest request = new TradeRequest(null, 2, TradeActionEnum.BUY);
        sendTrade(request, HttpStatus.BAD_REQUEST)
            .jsonPath("$.error").isEqualTo("Bad Request");

        request = new TradeRequest(TickerEnum.CIB, 2, null);
        sendTrade(request, HttpStatus.BAD_REQUEST)
            .jsonPath("$.error").isEqualTo("Bad Request");

        request = new TradeRequest(TickerEnum.CIB, null, TradeActionEnum.BUY);
        sendTrade(request, HttpStatus.BAD_REQUEST)
            .jsonPath("$.error").isEqualTo("Bad Request");
    }

    private void mockCustomerService(String path, int statusCode) {
        String responseBody = resourceToString(path);
        mockServerClient
            .when(HttpRequest.request()
                .withMethod(HttpMethod.POST.name())
                .withBody(RegexBody.regex(".*\"price\":150.*"))
                .withPath("/customers/1/trade"))
            .respond(HttpResponse.response(responseBody)
                .withStatusCode(statusCode)
                .withContentType(MediaType.APPLICATION_JSON)
            );
    }

    private void mockStockService(String path, int statusCode) {
        String responseBody = resourceToString(path);
        mockServerClient
            .when(HttpRequest.request().withPath("/stock/" + TickerEnum.GOOGLE))
            .respond(HttpResponse.response(responseBody)
                .withStatusCode(statusCode)
                .withContentType(MediaType.APPLICATION_JSON)
            );
    }

    private WebTestClient.BodyContentSpec sendTrade(TradeRequest request, HttpStatus status) {
        return this.webTestClient.post()
            .uri("/customers/{customer-id}/trade", 1)
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectBody()
            .consumeWith(response -> log.info("Response: {}",
                new String(Objects.requireNonNull(response.getResponseBody()))));
    }

}
