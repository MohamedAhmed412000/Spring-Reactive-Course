package com.project.webflux.traditional.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class TraditionalWebConfig {

    @Bean
    RestClient restClient() {
        String PRODUCTS_BASE_URL = "http://localhost:7070";
        return RestClient.builder()
            .requestFactory(new JdkClientHttpRequestFactory())
            .baseUrl(PRODUCTS_BASE_URL)
            .build();
    }

}
