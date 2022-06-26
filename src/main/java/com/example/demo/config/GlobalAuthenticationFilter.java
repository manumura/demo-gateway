package com.example.demo.config;

import com.example.demo.common.UserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class GlobalAuthenticationFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return exchange.getPrincipal()
//                .map(Principal::getName)
                .map(p -> {
//                    System.out.println("principal: " + p);
                    if (p instanceof UsernamePasswordAuthenticationToken) {
                        UsernamePasswordAuthenticationToken t = (UsernamePasswordAuthenticationToken) p;
                        if (t.getPrincipal() instanceof UserData) {
                            UserData u = (UserData) t.getPrincipal();
                            System.out.println("user: " + u);
                        }
                    }
                    return p.getName();
                })
                .map(username -> withBearerAuth(exchange, username))
                .defaultIfEmpty(exchange).flatMap(chain::filter);
    }

    private ServerWebExchange withBearerAuth(ServerWebExchange exchange, String username) {
        return exchange.mutate()
                .request(r -> r.headers(headers -> headers.add("X-client-name", username)))
                .build();
    }
}
