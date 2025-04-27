package com.project.customer.services;

import com.project.customer.domain.models.Customer;
import com.project.customer.domain.models.PortfolioItem;
import com.project.customer.dto.StockTradeRequest;
import com.project.customer.dto.StockTradeResponse;
import com.project.customer.exceptions.ApplicationExceptions;
import com.project.customer.mapppers.EntityDtoMapper;
import com.project.customer.repositories.CustomerRepository;
import com.project.customer.repositories.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradeService {

    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    @Transactional
    public Mono<StockTradeResponse> trade(Integer customerId, StockTradeRequest stockTradeRequest) {
        return switch (stockTradeRequest.action()) {
            case BUY -> this.buyStock(customerId, stockTradeRequest);
            case SELL -> this.sellStock(customerId, stockTradeRequest);
        };
    }

    private Mono<StockTradeResponse> buyStock(Integer customerId, StockTradeRequest request) {
        Mono<Customer> customerMono = this.customerRepository.findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId))
            .filter(customer -> customer.getBalance() >= request.totalPrice())
            .switchIfEmpty(ApplicationExceptions.InsufficientBalance(customerId));

        Mono<PortfolioItem> portfolioItemMono = this.portfolioItemRepository
            .findByCustomerIdAndTicker(customerId, request.ticker())
            .defaultIfEmpty(EntityDtoMapper.mapToPortfolioItem(customerId, request.ticker()));

        return customerMono.zipWhen(customer -> portfolioItemMono)
            .flatMap(tuple -> executeBuy(tuple.getT1(), tuple.getT2(), request));
    }

    private Mono<StockTradeResponse> executeBuy(Customer customer, PortfolioItem portfolioItem,
                                                StockTradeRequest request) {
        customer.setBalance(customer.getBalance() - request.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() + request.quantity());
        StockTradeResponse response = EntityDtoMapper.mapToTradeDto(customer, request);
        return Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
            .thenReturn(response);
    }

    private Mono<StockTradeResponse> sellStock(Integer customerId, StockTradeRequest request) {
        Mono<Customer> customerMono = this.customerRepository.findById(customerId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(customerId));

        Mono<PortfolioItem> portfolioItemMono = this.portfolioItemRepository
            .findByCustomerIdAndTicker(customerId, request.ticker())
            .defaultIfEmpty(EntityDtoMapper.mapToPortfolioItem(customerId, request.ticker()))
            .filter(portfolioItem -> portfolioItem.getQuantity() >= request.quantity())
            .switchIfEmpty(ApplicationExceptions.InsufficientShares(customerId));

        return customerMono.zipWhen(customer -> portfolioItemMono)
            .flatMap(tuple -> executeSell(tuple.getT1(), tuple.getT2(), request));
    }

    private Mono<StockTradeResponse> executeSell(Customer customer, PortfolioItem portfolioItem,
                                                StockTradeRequest request) {
        customer.setBalance(customer.getBalance() + request.totalPrice());
        portfolioItem.setQuantity(portfolioItem.getQuantity() - request.quantity());
        StockTradeResponse response = EntityDtoMapper.mapToTradeDto(customer, request);
        return Mono.zip(this.customerRepository.save(customer), this.portfolioItemRepository.save(portfolioItem))
            .thenReturn(response);
    }

}
