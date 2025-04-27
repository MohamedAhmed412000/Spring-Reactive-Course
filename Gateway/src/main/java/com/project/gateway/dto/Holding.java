package com.project.gateway.dto;

import com.project.gateway.domain.enums.TickerEnum;

public record Holding(
    TickerEnum ticker,
    Integer quantity
) {}
