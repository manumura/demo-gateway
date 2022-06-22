package com.example.demo.oauth2;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.ReactiveOpaqueTokenIntrospector;
import reactor.core.publisher.Mono;

public class Oauth2ReactiveOpaqueTokenIntrospectorImpl implements ReactiveOpaqueTokenIntrospector {

  private final Oauth2UserAuthenticator oauth2UserAuthenticator;

  public Oauth2ReactiveOpaqueTokenIntrospectorImpl(Oauth2UserAuthenticator oauth2UserAuthenticator) {
    this.oauth2UserAuthenticator = oauth2UserAuthenticator;
  }

  @Override
  public Mono<OAuth2AuthenticatedPrincipal> introspect(String token) {
    System.out.println("introspect token : " + token);
    return oauth2UserAuthenticator.getPrincipal(token);
  }
}
