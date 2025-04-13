package com.example.demo.config;

import com.example.demo.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.WebSessionServerSecurityContextRepository;

@Slf4j
@Configuration
@EnableWebFluxSecurity
public class WebSecurityConfig {

    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
    private final TokenUsernameExtractor tokenUsernameExtractor;

    private static final String[] WHITELIST_ENDPOINTS = {"/", "/public/**"};

    public WebSecurityConfig(ReactiveUserDetailsServiceImpl reactiveUserDetailsService, TokenUsernameExtractor tokenUsernameExtractor) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.tokenUsernameExtractor = tokenUsernameExtractor;
    }

    @Bean
    // UserDetailsRepositoryReactiveAuthenticationManager
    public UserDetailsReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new UserDetailsReactiveAuthenticationManager(reactiveUserDetailsService);
    }

    // https://stackoverflow.com/questions/78112501/sessionmanagement-and-csrf-cors-is-deprecated-since-version-6-1
    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http, CustomAuthenticationEntryPoint authenticationEntryPoint) {
        // Disable login form
        http
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .logout(ServerHttpSecurity.LogoutSpec::disable);

        // Custom security filter
        http
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .matchers(EndpointRequest.to("health", "info", "metrics")).permitAll()
                        .pathMatchers(WHITELIST_ENDPOINTS).permitAll()
                        .pathMatchers("/admin/**").hasAnyAuthority("ADMIN", "SUPER-ADMIN")
                        .pathMatchers("/super-admin/**").hasAuthority("SUPER-ADMIN")
                        .anyExchange().authenticated()
                )
                .addFilterBefore(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(authenticationEntryPoint)
                );

        return http.build();
    }

    private AuthenticationWebFilter authenticationWebFilter() {
        final AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(new TokenAuthenticationConverter(
                this.tokenUsernameExtractor));
        // Stateless: NoOpServerSecurityContextRepository
        authenticationWebFilter.setSecurityContextRepository(new WebSessionServerSecurityContextRepository());
        return authenticationWebFilter;
    }
}
