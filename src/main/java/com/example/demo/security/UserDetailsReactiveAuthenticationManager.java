package com.example.demo.security;

import com.example.demo.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
public class UserDetailsReactiveAuthenticationManager implements ReactiveAuthenticationManager {
  private final ReactiveUserDetailsService userDetailsService;

  public UserDetailsReactiveAuthenticationManager(ReactiveUserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @Override
  public Mono<Authentication> authenticate(final Authentication authentication) {
    if (authentication.isAuthenticated()) {
      return Mono.just(authentication);
    }

    return Mono.just(authentication)
        .switchIfEmpty(Mono.defer(this::raiseBadCredentialsException))
        .cast(UsernamePasswordAuthenticationToken.class)
        .flatMap(this::authenticateToken)
        .publishOn(Schedulers.parallel())
        .onErrorResume(e -> raiseBadCredentialsException())
        .map(u -> new UsernamePasswordAuthenticationToken(u, Constant.NA, u.getAuthorities()));
  }

  private <T> Mono<T> raiseBadCredentialsException() {
    return Mono.error(new BadCredentialsException("Invalid token"));
  }

  private Mono<UserDetails> authenticateToken(final UsernamePasswordAuthenticationToken authenticationToken) {
    String username = authenticationToken.getName();
    log.info("checking authentication for user {}", username);

    if (username != null) {
      log.info("authenticated user {}, setting security context", username);
      return this.userDetailsService.findByUsername(username);
    }

    return null;
  }
}
