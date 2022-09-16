package com.example.demo.filter;

import com.example.demo.dto.User;
import com.example.demo.security.TokenGeneratorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalBearerTokenFilter implements GlobalFilter {

    private final TokenGeneratorService tokenGeneratorService;

    public GlobalBearerTokenFilter(TokenGeneratorService tokenGeneratorService) {
        this.tokenGeneratorService = tokenGeneratorService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
                .mapNotNull(p -> {
                    if (p instanceof UsernamePasswordAuthenticationToken
                            && ((UsernamePasswordAuthenticationToken) p).getPrincipal() instanceof User) {
                        return  (User) ((UsernamePasswordAuthenticationToken) p).getPrincipal();
                    }
                    return null;
                })
                .map(user -> withBearerAuth(exchange, user))
                .defaultIfEmpty(exchange).flatMap(chain::filter);
    }

    private ServerWebExchange withBearerAuth(ServerWebExchange exchange, User user) {
        String token = tokenGeneratorService.generateToken(user);
        return exchange.mutate()
                .request(r -> r.headers(headers -> headers.set(HttpHeaders.AUTHORIZATION, "Bearer " + token)))
                .build();
    }
}
