package com.project.customer.dto;

import com.project.customer.domain.enums.TickerEnum;
import com.project.customer.domain.enums.TradeActionEnum;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import org.springframework.validation.annotation.Validated;

@Validated
public record StockTradeRequest(
    @NotNull TickerEnum ticker,
    @PositiveOrZero @NotNull Integer price,
    @Positive @NotNull Integer quantity,
    @NotNull TradeActionEnum action
) {

    public Integer totalPrice() {
        return price * quantity;
    }

}
