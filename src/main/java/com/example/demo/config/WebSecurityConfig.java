package com.example.demo.config;

import com.example.demo.oauth2.Oauth2UserAuthenticator;
import com.example.demo.security.GuacUserAuthenticator;
import com.example.demo.security.ReactiveUserDetailsServiceImpl;
import com.example.demo.security.TokenAuthenticationConverter;
import com.example.demo.security.UnauthorizedAuthenticationEntryPoint;
import com.example.demo.security.UserDetailsReactiveAuthenticationManager;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

@EnableWebFluxSecurity
public class WebSecurityConfig {

  private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
  private final GuacUserAuthenticator guacUserAuthenticator;
  private final Oauth2UserAuthenticator oauth2UserAuthenticator;

  // TODO remove: test no auth /config/topics
  private static final String[] WHITELIST_ENDPOINTS = { "/config/topics", "/config/topics/*", "/config/types/*" };
//  private static final String[] WHITELIST_ENDPOINTS = { "/config/topics/*", "/config/types/*" };

  public WebSecurityConfig(ReactiveUserDetailsServiceImpl reactiveUserDetailsService,
      GuacUserAuthenticator guacUserAuthenticator, Oauth2UserAuthenticator oauth2UserAuthenticator) {
    this.reactiveUserDetailsService = reactiveUserDetailsService;
    this.guacUserAuthenticator = guacUserAuthenticator;
    this.oauth2UserAuthenticator = oauth2UserAuthenticator;
  }

  private AuthenticationWebFilter authenticationWebFilter() {
    final AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
    // Without this permitAllMatcher, all requests with token in header will be intercepted by authentication filter
    final NegatedServerWebExchangeMatcher permitAllMatcher = new NegatedServerWebExchangeMatcher(
        ServerWebExchangeMatchers.pathMatchers(WHITELIST_ENDPOINTS));
    authenticationWebFilter.setRequiresAuthenticationMatcher(permitAllMatcher);
    authenticationWebFilter.setServerAuthenticationConverter(new TokenAuthenticationConverter(
        guacUserAuthenticator));
    // Stateless
    authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
    return authenticationWebFilter;
  }

  @Bean
  public UserDetailsReactiveAuthenticationManager reactiveAuthenticationManager() {
    return new UserDetailsReactiveAuthenticationManager(reactiveUserDetailsService);
  }

  @Bean
  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint) {
    // Disable login form
    http
        .httpBasic().disable()
        .formLogin().disable()
        .csrf().disable()
        .logout().disable();

    // Custom security filter
    http
        .authorizeExchange()
        .pathMatchers(HttpMethod.OPTIONS)
        .permitAll()
        .and()
        .authorizeExchange()
        .matchers(EndpointRequest.to("health", "info"))
        .permitAll()
        // TODO remove: test no auth
//        .and()
//        .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
//        .authorizeExchange()
//        .pathMatchers("/config/topics")
//        .hasAuthority("DISPATCH.TOASSIGN.ASSIGN")
////        .authenticated()
        .pathMatchers(WHITELIST_ENDPOINTS)
        .permitAll()
        .anyExchange()
        .authenticated();

    return http.build();
  }

  // https://www.baeldung.com/spring-security-5-reactive
  // https://github.com/raphaelDL/spring-webflux-security-jwt
  // https://andifalk.github.io/reactive-spring-security-5-workshop/workshop-tutorial.html
  // https://ldduy1006.medium.com/spring-webflux-security-configuration-28ac86423a42
  // https://www.naturalprogrammer.com/courses/332639/lectures/5902512
  // https://stackoverflow.com/questions/66366224/springboot-webflux-throwing-401-when-authorization-header-sent-for-unrestricted
//  @Bean
//  public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, UnauthorizedAuthenticationEntryPoint entryPoint) {
//    // Disable login form
//    http
//        .httpBasic().disable()
//        .formLogin().disable()
//        .csrf().disable()
//        .logout().disable();
//
//    // Oauth2 config
//    final ServerBearerTokenAuthenticationConverter serverBearerTokenAuthenticationConverter = new ServerBearerTokenAuthenticationConverter();
//    serverBearerTokenAuthenticationConverter.setBearerTokenHeaderName(Constant.GUAC_AUTHORIZATION_HEADER);
//
//    http
//        .exceptionHandling()
//        .authenticationEntryPoint(entryPoint)
//        .and()
//        .authorizeExchange()
//        .pathMatchers(HttpMethod.OPTIONS)
//        .permitAll()
//        .and()
//        .authorizeExchange()
//        .matchers(EndpointRequest.to("health", "info"))
//        .permitAll()
//        .and()
//        .authorizeExchange(
//            exchanges -> exchanges
//                .pathMatchers("/config/topics")
//                .hasAuthority("DISPATCH.TOASSIGN.ASSIGN")
////                .authenticated()
//                .pathMatchers("/config/**")
//                .permitAll()
//                .anyExchange()
//                .authenticated()
//        )
//        .oauth2ResourceServer()
//          .bearerTokenConverter(serverBearerTokenAuthenticationConverter)
//          .opaqueToken()
//          .introspector(new Oauth2ReactiveOpaqueTokenIntrospectorImpl(oauth2UserAuthenticator));
//
//    return http.build();
//  }
}
