package com.example.demo.security;

import reactor.core.publisher.Mono;

public interface TokenDecoderService {

    Mono<String> getUsernameFromToken(String token);
}
