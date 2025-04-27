package com.project.gateway.controllers;

import com.project.gateway.clients.StockServiceClient;
import com.project.gateway.dto.PriceUpdate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/stock")
@RequiredArgsConstructor
public class StockPriceStreamController {

    private final StockServiceClient stockServiceClient;

    @GetMapping(value = "/price-stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<PriceUpdate> getPriceUpdatesStream() {
        return stockServiceClient.getPriceUpdates();
    }

}
