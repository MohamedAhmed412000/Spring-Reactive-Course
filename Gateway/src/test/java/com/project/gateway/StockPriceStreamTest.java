package com.project.gateway;

import com.project.gateway.dto.PriceUpdate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@Slf4j
public class StockPriceStreamTest extends AbstractIntegrationTest {

    @Test
    public void testStockPriceStream() {
        // First mock stock service
        mockStockService("stock-service/stock-price-stream-200.json-nd", 200);
        
        getStockPriceStream()
            .doOnNext(price -> log.info("data: {}", price))
            .as(StepVerifier::create)
            .assertNext(data -> Assertions.assertEquals(53, data.price()))
            .assertNext(data -> Assertions.assertEquals(57, data.price()))
            .assertNext(data -> Assertions.assertEquals(52, data.price()))
            .expectComplete()
            .verify();
    }
    
    private void mockStockService(String path, int statusCode) {
        String responseBody = resourceToString(path);
        mockServerClient
            .when(HttpRequest.request().withPath("/stock/price-stream"))
            .respond(HttpResponse.response(responseBody)
                .withStatusCode(statusCode)
                .withContentType(MediaType.parse(org.springframework.http.MediaType.APPLICATION_NDJSON_VALUE))
            );
    }
    
    private Flux<PriceUpdate> getStockPriceStream() {
        return this.webTestClient.get()
            .uri("/stock/price-stream")
            .accept(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().isOk()
            .returnResult(PriceUpdate.class)
            .getResponseBody();
    }

}
