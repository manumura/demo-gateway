package com.example.demo.security;

import com.example.demo.constant.Constant;
import java.util.Collections;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class TokenUsernameExtractor {

  private final TokenService tokenService;

  public TokenUsernameExtractor(TokenService tokenService) {
    this.tokenService = tokenService;
  }

  public Mono<Authentication> getAuthentication(String token) {
    if (StringUtils.isBlank(token)) {
      return Mono.error(new BadCredentialsException("Invalid token"));
    }

    Mono<String> usernameMono = tokenService.getUsernameFromToken(token);
    return usernameMono
        .onErrorResume(e -> {
          log.warn(e.getMessage());
          return Mono.error(new BadCredentialsException("Invalid token"));
        })
        .filter(Objects::nonNull)
        .map(username -> new User(username, Constant.NA, Collections.emptyList()))
        .map(user -> new UsernamePasswordAuthenticationToken(user, Constant.NA));
  }
}
