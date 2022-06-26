package com.example.demo.security;

import com.example.demo.common.AuthenticatedUserData;
import com.example.demo.common.UserData;
import com.example.demo.constant.Constant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserService userService;

    public ReactiveUserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Mono<UserDetails> findByUsername(String userName) {
        return userService.getUserDetails(userName)
            .filter(Objects::nonNull)
            .switchIfEmpty(Mono.error(new UsernameNotFoundException(String.format("User %s not found", userName))))
            .map(this::createPrincipalUser);
    }

    private UserData createPrincipalUser(AuthenticatedUserData authenticatedUserData) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String authority : authenticatedUserData.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return new UserData(
                authenticatedUserData.getUsername(),
                Constant.NA,
                authorities,
                authenticatedUserData.getUuid(),
                authenticatedUserData.getFirstName(),
                authenticatedUserData.getLastName(),
                authenticatedUserData.getEmail(),
                authenticatedUserData.getPhoneNumber(),
                authenticatedUserData.getEnabled()
        );
    }
}
