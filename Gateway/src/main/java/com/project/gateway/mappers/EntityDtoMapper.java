package com.project.gateway.mappers;

import com.project.gateway.dto.StockTradeRequest;
import com.project.gateway.dto.TradeRequest;

public class EntityDtoMapper {

    public static StockTradeRequest constructTradeRequest(TradeRequest request, Integer price) {
        return new StockTradeRequest(
            request.ticker(),
            price,
            request.quantity(),
            request.action()
        );
    }

}
