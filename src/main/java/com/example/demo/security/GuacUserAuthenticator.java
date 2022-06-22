package com.example.demo.security;

import com.example.demo.client.GuacClient;
import com.example.demo.constant.Constant;
import java.util.Collections;
import java.util.Map;
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
public class GuacUserAuthenticator {

  private final GuacClient guacClient;

  public GuacUserAuthenticator(GuacClient guacClient) {
    this.guacClient = guacClient;
  }

  public Mono<Authentication> getAuthentication(String token) {

    System.out.println("token: " + token);

    if (StringUtils.isBlank(token)) {
      return Mono.error(new BadCredentialsException("Invalid token"));
    }

    Mono<Map<String, Object>> guacUserMono = guacClient.checkToken(token);
    return guacUserMono
        .onErrorResume(e -> {
          log.warn(e.getMessage());
          return Mono.error(new BadCredentialsException("Invalid token"));
        })
        .filter(Objects::nonNull)
        .filter(guacUser -> guacUser.get(Constant.USER_NAME) != null)
        .map(guacUser -> new User(guacUser.get(Constant.USER_NAME).toString(), Constant.NA, Collections.emptyList()))
        .map(user -> new UsernamePasswordAuthenticationToken(user, Constant.NA));
  }
}
