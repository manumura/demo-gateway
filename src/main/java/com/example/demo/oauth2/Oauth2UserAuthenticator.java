package com.example.demo.oauth2;

import com.example.demo.client.GuacClient;
import com.example.demo.client.UserManagementClient;
import com.example.demo.constant.Constant;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class Oauth2UserAuthenticator {

  private final GuacClient guacClient;
  private final UserManagementClient userManagementClient;

  public Oauth2UserAuthenticator(GuacClient guacClient, UserManagementClient userManagementClient) {
    this.guacClient = guacClient;
    this.userManagementClient = userManagementClient;
  }

  public Mono<OAuth2AuthenticatedPrincipal> getPrincipal(String token) {
    if (StringUtils.isBlank(token)) {
      return Mono.error(new BadCredentialsException("Invalid token"));
    }

    System.out.println("token " + token);

    Mono<Map<String, Object>> guacUserMono = guacClient.checkToken(token);
    return guacUserMono
        .onErrorResume(e -> {
          log.warn(e.getMessage());
//          return Mono.empty();
          return Mono.error(new BadCredentialsException("Invalid token"));
        })
        .filter(Objects::nonNull)
        .filter(guacUser -> guacUser.get(Constant.USER_NAME) != null)
        .flatMap(guacUser -> userManagementClient.getUserDetails(guacUser.get(Constant.USER_NAME).toString()))
        .filter(Objects::nonNull)
        .map(user -> {
          System.out.println("user " + user);
          return new Oauth2UserData(user);
          // Oauth2UserData::new;
        });
  }
}
