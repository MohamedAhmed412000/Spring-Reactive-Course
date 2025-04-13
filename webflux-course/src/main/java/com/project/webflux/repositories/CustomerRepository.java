package com.project.webflux.repositories;

import com.project.webflux.models.Customer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    Flux<Customer> findByEmailEndingWith(String email);

    @Modifying
    @Query("DELETE FROM CUSTOMER WHERE ID = :id")
    Mono<Boolean> deleteCustomersById(Integer id);

    Flux<Customer> findAllBy(Pageable pageable);

}
