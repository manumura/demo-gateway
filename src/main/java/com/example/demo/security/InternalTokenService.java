package com.example.demo.security;

import com.example.demo.dto.User;
import com.example.demo.properties.ApplicationProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.paseto.jpaseto.Paseto;
import dev.paseto.jpaseto.PasetoParser;
import dev.paseto.jpaseto.Pasetos;
import dev.paseto.jpaseto.lang.Keys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Slf4j
@Service
public class InternalTokenService {

    private static final String AUDIENCE = "internal";
    private static final String ISSUER = "gateway";
    private static final String USER_CLAIM = "user";
    private static final ObjectMapper mapper = new ObjectMapper();

    private final ApplicationProperties applicationProperties;

    public InternalTokenService(ApplicationProperties applicationProperties) {
        this.applicationProperties = applicationProperties;
    }

    public String generateToken(User user) {
        if (applicationProperties == null || applicationProperties.getToken() == null) {
            log.error("token properties not found");
            return null;
        }

        if (StringUtils.length(applicationProperties.getToken().getSalt()) != 32) {
            log.error("token salt length incorrect");
            return null;
        }

        SecretKey key = Keys.secretKey(applicationProperties.getToken().getSalt().getBytes(StandardCharsets.UTF_8));
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

    public User decodeToken(String token) {
        if (applicationProperties == null || applicationProperties.getToken() == null) {
            log.error("token properties not found");
            return null;
        }

        if (StringUtils.length(applicationProperties.getToken().getSalt()) != 32) {
            log.error("token salt length incorrect");
            return null;
        }

        SecretKey key = Keys.secretKey(applicationProperties.getToken().getSalt().getBytes(StandardCharsets.UTF_8));
        PasetoParser parser = Pasetos.parserBuilder()
                .setSharedSecret(key)
                .requireAudience(AUDIENCE)
//                .requireIssuer("gateway")
                .build();

        try {
            Paseto paseto = parser.parse(token);
            return mapper.convertValue(paseto.getClaims().get(USER_CLAIM), User.class);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
