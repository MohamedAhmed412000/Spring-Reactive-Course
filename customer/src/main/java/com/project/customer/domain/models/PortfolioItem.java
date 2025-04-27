package com.project.customer.domain.models;

import com.project.customer.domain.enums.TickerEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PORTFOLIO_ITEM")
public class PortfolioItem {

    @Id
    @Column(value = "ID")
    private Integer id;
    @Column(value = "CUSTOMER_ID")
    private Integer customerId;
    @Column(value = "TICKER")
    private TickerEnum ticker;
    @Column(value = "QUANTITY")
    private Integer quantity;
    
}
