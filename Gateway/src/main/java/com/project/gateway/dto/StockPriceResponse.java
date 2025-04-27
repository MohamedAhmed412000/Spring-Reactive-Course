package com.project.gateway.dto;

import com.project.gateway.domain.enums.TickerEnum;

public record StockPriceResponse(
    TickerEnum ticker,
    Integer price
) {}
