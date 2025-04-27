package com.project.gateway.dto;

import com.project.gateway.domain.enums.TickerEnum;
import com.project.gateway.domain.enums.TradeActionEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.validation.annotation.Validated;

@Validated
public record TradeRequest(
    @NotNull TickerEnum ticker,
    @Positive @NotNull Integer quantity,
    @NotNull TradeActionEnum action
) {}
