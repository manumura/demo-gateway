package com.example.demo.security;

import com.example.demo.constant.Constant;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class TokenAuthenticationConverter implements ServerAuthenticationConverter {

  private final TokenUsernameExtractor tokenUsernameExtractor;

  private static final String BEARER = "Bearer ";

  public TokenAuthenticationConverter(TokenUsernameExtractor tokenUsernameExtractor) {
    this.tokenUsernameExtractor = tokenUsernameExtractor;
  }

  @Override
  public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
    return Mono.justOrEmpty(serverWebExchange)
        .map(this::getTokenFromRequest)
        .filter(Objects::nonNull)
        .filter(header -> header.toLowerCase().startsWith(BEARER.toLowerCase()))
        .map(header -> header.substring(BEARER.length()))
        .filter(token -> !StringUtils.isBlank(token))
        .flatMap(tokenUsernameExtractor::getAuthentication)
        .filter(Objects::nonNull);
  }

  private String getTokenFromRequest(ServerWebExchange serverWebExchange) {
    String token = serverWebExchange.getRequest()
        .getHeaders()
        .getFirst(Constant.AUTHORIZATION_HEADER);
    return StringUtils.isBlank(token) ? "" : token;
  }
}
