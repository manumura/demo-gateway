package com.example.demo.config;

import com.example.demo.security.*;
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
    private final TokenUsernameExtractor tokenUsernameExtractor;

    private static final String[] WHITELIST_ENDPOINTS = {"/public/**"};

    public WebSecurityConfig(ReactiveUserDetailsServiceImpl reactiveUserDetailsService, TokenUsernameExtractor tokenUsernameExtractor) {
        this.reactiveUserDetailsService = reactiveUserDetailsService;
        this.tokenUsernameExtractor = tokenUsernameExtractor;
    }

    @Bean
    public UserDetailsReactiveAuthenticationManager reactiveAuthenticationManager() {
        return new UserDetailsReactiveAuthenticationManager(reactiveUserDetailsService);
    }

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
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
                .matchers(EndpointRequest.to("health", "info", "metrics"))
                .permitAll()

                .pathMatchers(WHITELIST_ENDPOINTS)
                .permitAll()

                .and()
                .addFilterAt(authenticationWebFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange()
                .pathMatchers("/admin/**")
                .hasAnyAuthority("ADMIN", "SUPER-ADMIN")
                .pathMatchers("/super-admin/**")
                .hasAuthority("SUPER-ADMIN")

                .anyExchange()
                .authenticated();

        return http.build();
    }

    private AuthenticationWebFilter authenticationWebFilter() {
        final AuthenticationWebFilter authenticationWebFilter = new AuthenticationWebFilter(reactiveAuthenticationManager());
        authenticationWebFilter.setServerAuthenticationConverter(new TokenAuthenticationConverter(
                this.tokenUsernameExtractor));
        // Stateless
        authenticationWebFilter.setSecurityContextRepository(NoOpServerSecurityContextRepository.getInstance());
        return authenticationWebFilter;
    }
}
