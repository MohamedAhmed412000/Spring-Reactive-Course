package com.project.webflux.config;

import com.project.webflux.dto.ProductDto;
import com.project.webflux.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class DataSetupConfig implements CommandLineRunner {

    private final ProductService productService;

    @Override
    public void run(String... args) throws Exception {
        Flux.range(1, 1_000)
            .delayElements(Duration.ofSeconds(1))
            .map(i -> new ProductDto(null, "Product " + i,
                ThreadLocalRandom.current().nextInt(1, 2000)))
            .flatMap(productDto -> productService.saveProduct(Mono.just(productDto)))
            .subscribe();
    }

}
