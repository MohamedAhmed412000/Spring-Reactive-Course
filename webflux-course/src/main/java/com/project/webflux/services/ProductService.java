package com.project.webflux.services;

import com.project.webflux.dto.ProductDto;
import com.project.webflux.mappers.ProductMapper;
import com.project.webflux.models.Product;
import com.project.webflux.repositories.ProductRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Data
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final Sinks.Many<ProductDto> productsSink;

    public Flux<ProductDto> getAllProducts() {
        return this.productRepository.findAll()
            .map(product -> ProductMapper.convertToProductDto(product, null));
    }

    public Mono<ProductDto> saveProduct(Mono<ProductDto> productDtoMono) {
        return productDtoMono.map(productDto ->
            ProductMapper.convertToProduct(productDto, new Product()))
            .flatMap(this.productRepository::save)
            .map(product -> ProductMapper.convertToProductDto(product, null))
            .doOnNext(this.productsSink::tryEmitNext);
    }

    public Flux<ProductDto> getProductStream() {
        return this.productsSink.asFlux();
    }

    public Flux<ProductDto> saveProducts(Flux<ProductDto> productsDtoFlux) {
        return productsDtoFlux.map(productDto ->
            ProductMapper.convertToProduct(productDto, new Product()))
            .as(this.productRepository::saveAll)
            .map(product -> ProductMapper.convertToProductDto(product, null));
    }

    public Mono<Long> getProductsCount() {
        return this.productRepository.count();
    }

}
