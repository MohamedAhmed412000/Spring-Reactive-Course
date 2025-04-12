package com.project.webflux.repositories;

import com.project.webflux.models.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    Flux<Product> findProductsByPriceBetween(Integer from, Integer to);
    Flux<Product> findAllBy(Pageable pageable);

}
