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

@Data
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Flux<ProductDto> getAllProducts() {
        return this.productRepository.findAll()
            .map(product -> ProductMapper.convertToProductDto(product, null));
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
