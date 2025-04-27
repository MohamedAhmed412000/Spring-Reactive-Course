package com.project.customer.domain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CUSTOMER")
public class Customer {

    @Id
    @Column(value = "ID")
    private Integer id;
    @Column(value = "NAME")
    private String name;
    @Column(value = "BALANCE")
    private Integer balance;

}
