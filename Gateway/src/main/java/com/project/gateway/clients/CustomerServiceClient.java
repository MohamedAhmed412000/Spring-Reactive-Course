package com.project.gateway.clients;

import com.project.gateway.dto.CustomerInformation;
import com.project.gateway.dto.StockTradeRequest;
import com.project.gateway.dto.StockTradeResponse;
import com.project.gateway.exceptions.ApplicationExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class CustomerServiceClient {

    private final WebClient webClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return this.webClient.get()
            .uri("/customers/{customer-id}", customerId)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(CustomerInformation.class)
            .onErrorResume(WebClientResponseException.NotFound.class,
                ex -> ApplicationExceptions.customerNotFound(customerId));
    }

    public Mono<StockTradeResponse> stockTrade(Integer customerId, StockTradeRequest request) {
        return this.webClient.post()
            .uri("/customers/{customer-id}/trade", customerId)
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(request)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToMono(StockTradeResponse.class)
            .onErrorResume(WebClientResponseException.NotFound.class,
                ex -> ApplicationExceptions.customerNotFound(customerId))
            .onErrorResume(WebClientResponseException.BadRequest.class, this::handleException);
    }

    private <T> Mono<T> handleException(WebClientResponseException.BadRequest exception) {
        ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
        String message = Objects.isNull(problemDetail)? exception.getMessage() : problemDetail.getDetail();
        log.error("Error occurred while processing request: {} ", message);
        return ApplicationExceptions.invalidTrade(message);
    }

}
