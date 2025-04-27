package com.project.customer.controllers;

import com.project.customer.dto.CustomerInformation;
import com.project.customer.dto.StockTradeRequest;
import com.project.customer.dto.StockTradeResponse;
import com.project.customer.services.CustomerService;
import com.project.customer.services.TradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final TradeService tradeService;

    @GetMapping("/{customer-id}")
    public Mono<CustomerInformation> getCustomerInformation(
        @PathVariable("customer-id") Integer customerId
    ) {
        return customerService.getCustomerInformation(customerId);
    }

    @PostMapping("/{customer-id}/trade")
    public Mono<StockTradeResponse> trade(
        @PathVariable("customer-id") Integer customerId,
        @Valid @RequestBody Mono<StockTradeRequest> stockTradeRequestMono
    ) {
        return stockTradeRequestMono
            .flatMap(stockTradeRequest -> tradeService.trade(customerId, stockTradeRequest));
    }

}
