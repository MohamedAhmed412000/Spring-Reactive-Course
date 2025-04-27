package com.project.gateway.config;

import com.project.gateway.clients.CustomerServiceClient;
import com.project.gateway.clients.StockServiceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Configuration
public class ServiceClientsConfig {

    @Bean
    public CustomerServiceClient getCustomerServiceClient(
        @Value("${customer.service.url}") String baseUrl
    ) {
        return new CustomerServiceClient(createWebClient(baseUrl));
    }

    @Bean
    public StockServiceClient getStockServiceClient(
        @Value("${stock.service.url}") String baseUrl
    ) {
        return new StockServiceClient(createWebClient(baseUrl));
    }

    private WebClient createWebClient(String baseUrl) {
        log.info("Creating web client with url {}", baseUrl);
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

}
