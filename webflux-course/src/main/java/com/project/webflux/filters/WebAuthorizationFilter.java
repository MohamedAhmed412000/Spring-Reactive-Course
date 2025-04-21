package com.project.webflux.filters;

import com.project.webflux.enums.AuthorizationTypesEnum;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Order(2)
@Service
public class WebAuthorizationFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        AuthorizationTypesEnum authType = (AuthorizationTypesEnum) exchange.getRequest().getAttributes()
            .getOrDefault("userType", AuthorizationTypesEnum.STANDARD);
        return switch (authType) {
            case STANDARD -> filterStandard(exchange, chain);
            case PREMIUM -> chain.filter(exchange);
        };
    }

    private Mono<Void> filterStandard(ServerWebExchange exchange, WebFilterChain chain) {
        if (HttpMethod.GET.equals(exchange.getRequest().getMethod())) {
            return chain.filter(exchange);
        }
        return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));
    }

}
