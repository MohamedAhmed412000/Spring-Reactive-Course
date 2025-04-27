package com.project.customer.dto;

import com.project.customer.domain.enums.TickerEnum;
import com.project.customer.domain.enums.TradeActionEnum;

public record StockTradeResponse(
   Integer customerId,
   TickerEnum ticker,
   Integer price,
   Integer quantity,
   TradeActionEnum action,
   Integer totalPrice,
   Integer balance
) {}
