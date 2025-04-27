package com.project.gateway.controllers;

import com.project.gateway.dto.CustomerInformation;
import com.project.gateway.dto.StockTradeResponse;
import com.project.gateway.dto.TradeRequest;
import com.project.gateway.services.CustomerPortfolioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerPortfolioService customerPortfolioService;

    @GetMapping("/{customer-id}")
    public Mono<CustomerInformation> getCustomerInformation(
        @PathVariable("customer-id") Integer customerId
    ) {
        return customerPortfolioService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customer-id}/trade")
    public Mono<StockTradeResponse> createTrade(
        @PathVariable("customer-id") Integer customerId,
        @Valid @RequestBody Mono<TradeRequest> tradeRequestMono
    ) {
        return tradeRequestMono.flatMap(tradeRequest ->
            this.customerPortfolioService.tradeStock(customerId, tradeRequest)
        );
    }

}
