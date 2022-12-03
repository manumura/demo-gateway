package com.example.demo.security.impl;

import com.example.demo.constant.Constant;
import com.example.demo.dto.User;
import com.example.demo.properties.ApplicationProperties;
import com.example.demo.security.TokenGeneratorService;
import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.lang.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;

@Slf4j
@Service
public class PasetoTokenGeneratorServiceImpl implements TokenGeneratorService {

    private static final String AUDIENCE = "internal";
    private static final String ISSUER = "gateway";
    private static final String USER_CLAIM = "user";

    private final ApplicationProperties applicationProperties;

    public PasetoTokenGeneratorServiceImpl(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String generateToken(User user) {
        if (applicationProperties == null || applicationProperties.getToken() == null) {
            log.error("token properties not found");
            return null;
        }

        if (StringUtils.length(applicationProperties.getToken().getKey()) != 32) {
            log.error("token key length incorrect");
            return null;
        }

        SecretKey key = Keys.secretKey(applicationProperties.getToken().getKey().getBytes(StandardCharsets.UTF_8));
        Instant now = Instant.now();

        return Pasetos.V2.LOCAL.builder()
                .setSharedSecret(key)
                .setIssuedAt(now)
                .setExpiration(now.plus(applicationProperties.getToken().getExpiryInSec(), ChronoUnit.SECONDS))
                .setAudience(AUDIENCE)
                .setIssuer(ISSUER)
                .setSubject(user == null ? "" : user.getUsername())
                .claim(USER_CLAIM, user)
                .compact();
    }

    public String generateInternalUserToken() {
        User user = new User("internal", Constant.NA, Set.of(new SimpleGrantedAuthority("INTERNAL")));
        String token = generateToken(user);
        log.debug("paseto token for internal user: {}", token);
        return token;
    }
}
