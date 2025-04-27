package com.project.gateway.dto;

import com.project.gateway.domain.enums.TickerEnum;

import java.time.LocalDateTime;

public record PriceUpdate(
    TickerEnum ticker,
    Integer price,
    LocalDateTime time
) {}
