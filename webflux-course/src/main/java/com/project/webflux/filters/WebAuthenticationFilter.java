package com.project.webflux.filters;

import com.project.webflux.enums.AuthorizationTypesEnum;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Order(1)
@Service
public class WebAuthenticationFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authorization = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (Objects.nonNull(authorization)) {
            AuthorizationTypesEnum userType = AuthorizationTypesEnum.fromValue(authorization);
            if (Objects.nonNull(userType)) {
                exchange.getAttributes().put("userType", userType);
                return chain.filter(exchange);
            }
        }
        return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
    }

}
