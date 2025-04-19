package com.project.webflux.repositories;

import com.project.webflux.dto.OrderDetails;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.test.StepVerifier;

@Slf4j
public class DatabaseClientTest extends AbstractTest {

    @Autowired
    private DatabaseClient databaseClient;

    @Test
    public void orderDetailsByProductName() {
        String dbQuery = "SELECT O.ORDER_ID, C.NAME AS CUSTOMER_NAME, P.DESCRIPTION AS PRODUCT_NAME, " +
            "O.AMOUNT, O.ORDER_DATE FROM \"ORDER\" O " +
            "JOIN CUSTOMER C on C.ID = O.CUSTOMER_ID " +
            "JOIN PRODUCT P on P.ID = O.PRODUCT_ID " +
            "WHERE P.DESCRIPTION = :description";
        this.databaseClient.sql(dbQuery).bind("description", "iphone 18")
            .mapProperties(OrderDetails.class).all()
            .doOnNext(orderDetails -> log.info("Order received: {}", orderDetails))
            .as(StepVerifier::create)
            .expectNextCount(3)
            .expectComplete()
            .verify();
    }

}
