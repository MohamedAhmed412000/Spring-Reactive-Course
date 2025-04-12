package com.project.webflux.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ORDER")
public class Order {

    @Id
    @Column(value = "ORDER_ID")
    private UUID id;
    @Column(value = "CUSTOMER_ID")
    private Integer customerId;
    @Column(value = "PRODUCT_ID")
    private Integer productId;
    @Column(value = "AMOUNT")
    private Integer amount;
    @Column(value = "ORDER_DATE")
    private Instant orderDate;

}
