package com.project.webflux.services;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.function.Consumer;

public abstract class AbstractWebClient {

    protected WebClient createWebClient() {
        return createWebClient(builder -> {});
    }

    protected WebClient createWebClient(Consumer<WebClient.Builder> builderConsumer) {
        WebClient.Builder webClientBuilder = WebClient.builder()
            .baseUrl("http://localhost:7070/demo02");
        builderConsumer.accept(webClientBuilder);
        return webClientBuilder.build();
    }

}
