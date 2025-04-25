package com.project.webflux.controllers;

import com.project.webflux.dto.ProductDto;
import com.project.webflux.dto.UploadResponse;
import com.project.webflux.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping(path = "/upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponse> upload(@RequestBody Flux<ProductDto> productsFlux) {
        log.info("Uploading product data");
        return this.productService.saveProducts(
            productsFlux
//                .doOnNext(productDto -> log.info("Uploading product: {}", productDto))
            )
            .then(this.productService.getProductsCount())
            .map(count -> new UploadResponse(UUID.randomUUID(), count));
    }

    @PostMapping(path = "/download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDto> downloadAllProducts() {
        log.info("Downloading all products");
        return this.productService.getAllProducts();
    }

}
