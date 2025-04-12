package com.project.webflux.repositories;

import com.project.webflux.dto.OrderDetails;
import com.project.webflux.models.Order;
import com.project.webflux.models.Product;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {

    @Query("SELECT P.* FROM PRODUCT P " +
        "JOIN \"ORDER\" O on P.ID = O.PRODUCT_ID " +
        "JOIN CUSTOMER C on C.ID = O.CUSTOMER_ID " +
        "WHERE C.NAME = :name")
    Flux<Product> findAllProductsOrderedByCustomerName(String name);

    @Query("SELECT O.ORDER_ID, C.NAME AS CUSTOMER_NAME, P.DESCRIPTION AS PRODUCT_NAME, O.AMOUNT, O.ORDER_DATE" +
        " FROM \"ORDER\" O " +
        "JOIN CUSTOMER C on C.ID = O.CUSTOMER_ID " +
        "JOIN PRODUCT P on P.ID = O.PRODUCT_ID " +
        "WHERE P.DESCRIPTION = :description"
    )
    Flux<OrderDetails> findAllOrdersByProductDesc(String description);

}
