package com.project.webflux.traditional.controller;

import com.project.webflux.traditional.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@RequestMapping("/traditional")
@RequiredArgsConstructor
@Slf4j
public class TraditionalController {

    private final RestClient restClient;

    @GetMapping("/products")
    List<ProductDto> getProducts() {
        List<ProductDto> products = this.restClient.get()
            .uri("/demo01/products")
            .retrieve()
            .body(new ParameterizedTypeReference<List<ProductDto>>() {});
        log.info("received response: {}", products);
        return products;
    }

    @GetMapping(value = "/products/fail")
    List<ProductDto> getProductsFailing() {
        List<ProductDto> products = this.restClient.get()
            .uri("/demo01/products/notorious")
            .retrieve()
            .body(new ParameterizedTypeReference<List<ProductDto>>() {});
        log.info("received response: {}", products);
        return products;
    }

}
