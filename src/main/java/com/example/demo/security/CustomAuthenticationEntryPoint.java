package com.example.demo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
@Slf4j
@Component
public class CustomAuthenticationEntryPoint implements ServerAuthenticationEntryPoint {
  @Override
  public Mono<Void> commence(final ServerWebExchange exchange, final AuthenticationException e) {
    log.error("AuthenticationException: ", e);
    return Mono.fromRunnable(() -> exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
  }
}
