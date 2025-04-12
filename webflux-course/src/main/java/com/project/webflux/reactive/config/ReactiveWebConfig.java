package com.project.webflux.reactive.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ReactiveWebConfig {

    @Bean
    WebClient webClient() {
        String PRODUCTS_BASE_URL = "http://localhost:7070";
        return WebClient.builder()
            .baseUrl(PRODUCTS_BASE_URL)
            .build();
    }

}
