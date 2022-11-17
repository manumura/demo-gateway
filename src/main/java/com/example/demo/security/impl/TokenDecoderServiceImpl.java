package com.example.demo.security.impl;

import com.example.demo.security.TokenDecoderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class TokenDecoderServiceImpl implements TokenDecoderService {

    public Mono<String> getUsernameFromToken(String token) {
        System.err.println("token: " + token);
        // TODO : hardcode for testing
        if ("my-secret-token".equals(token)) {
            return Mono.just("manumura");
        }
        if ("my-secret-admin-token".equals(token)) {
            return Mono.just("manumura-admin");
        }
        if ("my-secret-super-admin-token".equals(token)) {
            return Mono.just("manumura-super-admin");
        }
        return Mono.empty();
    }
}
