package com.project.customer;

import com.project.customer.domain.enums.TickerEnum;
import com.project.customer.domain.enums.TradeActionEnum;
import com.project.customer.dto.StockTradeRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;

@Slf4j
@AutoConfigureWebTestClient
@SpringBootTest(properties = "logging.level.org.springframework.r2dbc=debug")
class CustomerApplicationTests {

    @Autowired
    private WebTestClient client;

    @Test
    public void customerInformation() {
        this.getCustomer(1, HttpStatus.OK)
            .jsonPath("$.id").isEqualTo(1)
            .jsonPath("$.name").isEqualTo("Mohamed")
            .jsonPath("$.balance").isEqualTo(10000)
            .jsonPath("$.holdings").isEmpty();
    }

    @Test
    public void customerNotFound() {
        this.getCustomer(111, HttpStatus.NOT_FOUND);
    }

    @Test
    public void buyShares() {
        StockTradeRequest request = new StockTradeRequest(TickerEnum.GOLD, 500, 4, TradeActionEnum.BUY);
        this.trade(2, HttpStatus.OK, request);

        request = new StockTradeRequest(TickerEnum.GOLD, 280, 3, TradeActionEnum.BUY);
        this.trade(2, HttpStatus.OK, request);

        this.getCustomer(2, HttpStatus.OK)
            .jsonPath("$.id").isEqualTo(2)
            .jsonPath("$.name").isEqualTo("Ahmed")
            .jsonPath("$.balance").isEqualTo(7160)
            .jsonPath("$.holdings").isNotEmpty();
    }

    @Test
    public void buyAndSell() {
        StockTradeRequest request = new StockTradeRequest(TickerEnum.GOLD, 500, 4, TradeActionEnum.BUY);
        this.trade(3, HttpStatus.OK, request)
            .jsonPath("$.customerId").isEqualTo(3)
            .jsonPath("$.quantity").isEqualTo(4)
            .jsonPath("$.price").isEqualTo(500)
            .jsonPath("$.action").isEqualTo(TradeActionEnum.BUY)
            .jsonPath("$.ticker").isEqualTo(TickerEnum.GOLD)
            .jsonPath("$.totalPrice").isEqualTo(2000)
            .jsonPath("$.balance").isEqualTo(8000);

        request = new StockTradeRequest(TickerEnum.GOLD, 600, 2, TradeActionEnum.SELL);
        this.trade(3, HttpStatus.OK, request)
            .jsonPath("$.customerId").isEqualTo(3)
            .jsonPath("$.quantity").isEqualTo(2)
            .jsonPath("$.price").isEqualTo(600)
            .jsonPath("$.action").isEqualTo(TradeActionEnum.SELL)
            .jsonPath("$.ticker").isEqualTo(TickerEnum.GOLD)
            .jsonPath("$.totalPrice").isEqualTo(1200)
            .jsonPath("$.balance").isEqualTo(9200);
    }

    @Test
    public void invalidBuy() {
        StockTradeRequest request = new StockTradeRequest(TickerEnum.GOLD, 5000, 4, TradeActionEnum.BUY);
        this.trade(2, HttpStatus.FORBIDDEN, request);
    }

    @Test
    public void invalidSell() {
        StockTradeRequest request = new StockTradeRequest(TickerEnum.CIB, 8500, 3, TradeActionEnum.SELL);
        this.trade(2, HttpStatus.FORBIDDEN, request);
    }

    private WebTestClient.BodyContentSpec getCustomer(Integer customerId, HttpStatus status) {
        return this.client.get()
            .uri("/customers/{customer-id}", customerId)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectHeader().contentType(status == HttpStatus.OK?
                MediaType.APPLICATION_JSON: MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody().consumeWith(response ->
                log.info("Customer information: {}", Objects.requireNonNull(response)));
    }

    private WebTestClient.BodyContentSpec trade(Integer customerId, HttpStatus status, StockTradeRequest request) {
        return this.client.post()
            .uri("/customers/{customer-id}/trade", customerId)
            .bodyValue(request)
            .exchange()
            .expectStatus().isEqualTo(status)
            .expectHeader().contentType(status == HttpStatus.OK?
                MediaType.APPLICATION_JSON: MediaType.APPLICATION_PROBLEM_JSON)
            .expectBody()
            .consumeWith(response ->
                log.info("Trade information: {}", Objects.requireNonNull(response)));
    }

}