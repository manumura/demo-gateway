package com.example.demo.security;

import com.example.demo.constant.Constant;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class AuthenticationMatcher implements ServerWebExchangeMatcher {

  @Override
  public Mono<MatchResult> matches(final ServerWebExchange exchange) {
    Mono<ServerHttpRequest> request = Mono.just(exchange).map(ServerWebExchange::getRequest);

    /* Check for header "GUAC-Authorization" */
    return request.map(ServerHttpRequest::getHeaders)
        .filter(h -> h.containsKey(Constant.GUAC_AUTHORIZATION_HEADER))
        .flatMap(h -> MatchResult.match())
        .switchIfEmpty(MatchResult.notMatch());
  }

}
