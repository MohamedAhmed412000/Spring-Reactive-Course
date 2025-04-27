package com.project.gateway.services;

import com.project.gateway.clients.CustomerServiceClient;
import com.project.gateway.clients.StockServiceClient;
import com.project.gateway.dto.CustomerInformation;
import com.project.gateway.dto.StockPriceResponse;
import com.project.gateway.dto.TradeRequest;
import com.project.gateway.dto.StockTradeResponse;
import com.project.gateway.mappers.EntityDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerPortfolioService {

    private final CustomerServiceClient customerServiceClient;
    private final StockServiceClient stockServiceClient;

    public Mono<CustomerInformation> getCustomerInformation(Integer customerId) {
        return customerServiceClient.getCustomerInformation(customerId);
    }

    public Mono<StockTradeResponse> tradeStock(Integer customerId, TradeRequest request) {
        return stockServiceClient.getStockPrice(request.ticker())
            .map(StockPriceResponse::price)
            .map(price -> EntityDtoMapper.constructTradeRequest(request, price))
            .flatMap(stockTradeRequest ->
                customerServiceClient.stockTrade(customerId, stockTradeRequest));
    }

}
