package com.project.customer.repositories;

import com.project.customer.domain.enums.TickerEnum;
import com.project.customer.domain.models.PortfolioItem;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {

    @Query("SELECT pi.* FROM PORTFOLIO_ITEM pi WHERE pi.CUSTOMER_ID = :customerId")
    Flux<PortfolioItem> findAllByCustomerId(int customerId);

    @Query("SELECT pi.* FROM PORTFOLIO_ITEM pi WHERE pi.CUSTOMER_ID = :customerId AND pi.TICKER = :ticker")
    Mono<PortfolioItem> findByCustomerIdAndTicker(int customerId, TickerEnum ticker);

}
