package com.project.webflux.mappers;

import com.project.webflux.dto.ProductDto;
import com.project.webflux.models.Product;

public class ProductMapper {

    public static Product convertToProduct(ProductDto productDto, Product product) {
        product.setId(productDto.id());
        product.setDescription(productDto.description());
        product.setPrice(productDto.price());
        return product;
    }

    public static ProductDto convertToProductDto(Product product, ProductDto productDto) {
        return ProductDto.builder()
            .id(product.getId())
            .description(product.getDescription())
            .price(product.getPrice())
            .build();
    }

}
