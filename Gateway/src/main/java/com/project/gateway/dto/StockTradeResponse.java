package com.project.gateway.dto;

import com.project.gateway.domain.enums.TickerEnum;
import com.project.gateway.domain.enums.TradeActionEnum;

public record StockTradeResponse(
   Integer customerId,
   TickerEnum ticker,
   Integer price,
   Integer quantity,
   TradeActionEnum action,
   Integer totalPrice,
   Integer balance
) {}
