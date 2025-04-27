package com.project.customer.mapppers;

import com.project.customer.domain.enums.TickerEnum;
import com.project.customer.domain.models.Customer;
import com.project.customer.domain.models.PortfolioItem;
import com.project.customer.dto.CustomerInformation;
import com.project.customer.dto.Holding;
import com.project.customer.dto.StockTradeRequest;
import com.project.customer.dto.StockTradeResponse;

import java.util.List;

public class EntityDtoMapper {

    private EntityDtoMapper() {}

    public static CustomerInformation mapToCustomerDto(Customer customer, List<PortfolioItem> portfolioItems) {
        return new CustomerInformation(
            customer.getId(), customer.getName(), customer.getBalance(), portfolioItems.stream().map(
                portfolioItem -> new Holding(portfolioItem.getTicker(), portfolioItem.getQuantity())
        ).toList());
    }

    public static PortfolioItem mapToPortfolioItem(Integer customerId, TickerEnum ticker) {
        PortfolioItem portfolioItem = new PortfolioItem();
        portfolioItem.setCustomerId(customerId);
        portfolioItem.setTicker(ticker);
        portfolioItem.setQuantity(0);
        return portfolioItem;
    }

    public static StockTradeResponse mapToTradeDto(Customer customer, StockTradeRequest request) {
        return new StockTradeResponse(
            customer.getId(),
            request.ticker(),
            request.price(),
            request.quantity(),
            request.action(),
            request.totalPrice(),
            customer.getBalance()
        );
    }

}
