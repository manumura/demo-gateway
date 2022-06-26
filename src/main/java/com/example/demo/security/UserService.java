package com.example.demo.security;

import com.example.demo.common.AuthenticatedUserData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
public class UserService {

    public Mono<AuthenticatedUserData> getUserDetails(final String username) {
        // TODO : hardcode for testing
        if ("manumura".equals(username)) {
            return Mono.just(AuthenticatedUserData.builder()
                    .uuid("1")
                    .username("manumura")
                    .firstName("manu")
                    .lastName("mura")
                    .email("manumura@yahoo.com")
                    .enabled(true)
                    .phoneNumber("0123456789")
                    .authorities(List.of("USER"))
                    .build());
        }
        if ("manumura-admin".equals(username)) {
            return Mono.just(AuthenticatedUserData.builder()
                    .uuid("1")
                    .username("manumura-admin")
                    .firstName("manu")
                    .lastName("mura")
                    .email("manumura@yahoo.com")
                    .enabled(true)
                    .phoneNumber("0123456789")
                    .authorities(List.of("ADMIN"))
                    .build());
        }
        if ("manumura-super-admin".equals(username)) {
            return Mono.just(AuthenticatedUserData.builder()
                    .uuid("1")
                    .username("manumura-super-admin")
                    .firstName("manu")
                    .lastName("mura")
                    .email("manumura@yahoo.com")
                    .enabled(true)
                    .phoneNumber("0123456789")
                    .authorities(List.of("SUPER-ADMIN"))
                    .build());
        }
        return Mono.empty();
    }

}
