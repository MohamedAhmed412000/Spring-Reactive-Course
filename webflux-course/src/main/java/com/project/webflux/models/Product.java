package com.project.webflux.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "PRODUCT")
public class Product {

    @Id
    @Column(value = "ID")
    private Integer id;
    @Column(value = "DESCRIPTION")
    private String description;
    @Column(value = "PRICE")
    private Integer price;

}
