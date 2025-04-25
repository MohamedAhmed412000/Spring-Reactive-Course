package com.project.webflux.config;

import com.project.webflux.dto.ProductDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {

    @Bean
    public Sinks.Many<ProductDto> productSink() {
        return Sinks.many().replay().limit(1);
    }

}
