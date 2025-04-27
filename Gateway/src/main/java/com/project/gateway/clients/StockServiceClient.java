package com.project.gateway.clients;

import com.project.gateway.domain.enums.TickerEnum;
import com.project.gateway.dto.PriceUpdate;
import com.project.gateway.dto.StockPriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class StockServiceClient {

    private final WebClient webClient;
    private Flux<PriceUpdate> priceUpdateFlux;

    public Mono<StockPriceResponse> getStockPrice(TickerEnum ticker) {
        return this.webClient.get()
            .uri("/stock/{ticker}", ticker)
            .accept(MediaType.APPLICATION_JSON)
            .exchangeToMono(response -> response.bodyToMono(StockPriceResponse.class));
    }

    public Flux<PriceUpdate> getPriceUpdates() {
        if (Objects.isNull(priceUpdateFlux)) {
            priceUpdateFlux = getPriceUpdate();
        }
        return priceUpdateFlux;
    }

    private Flux<PriceUpdate> getPriceUpdate() {
        return this.webClient.get()
            .uri("/stock/price-stream")
            .accept(MediaType.APPLICATION_NDJSON)
            .exchangeToFlux(response -> response.bodyToFlux(PriceUpdate.class))
            .retryWhen(retry())
            .cache(1);
    }

    private Retry retry() {
        return  Retry.fixedDelay(100, Duration.ofSeconds(1))
            .doBeforeRetry(retrySignal -> log.error("Stock service price stream call failed with this " +
                "error: {}", retrySignal.failure().getMessage()));
    }

}
