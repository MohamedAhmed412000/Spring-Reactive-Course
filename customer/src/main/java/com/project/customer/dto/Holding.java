package com.project.customer.dto;

import com.project.customer.domain.enums.TickerEnum;

public record Holding(
    TickerEnum ticker,
    Integer quantity
) {}
