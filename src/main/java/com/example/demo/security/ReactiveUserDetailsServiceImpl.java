package com.example.demo.security;

import com.example.demo.dto.AuthenticatedUser;
import com.example.demo.dto.User;
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

    private User createPrincipalUser(AuthenticatedUser authenticatedUser) {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        for (String authority : authenticatedUser.getAuthorities()) {
            authorities.add(new SimpleGrantedAuthority(authority));
        }
        return new User(
                authenticatedUser.getUsername(),
                Constant.NA,
                authorities,
                authenticatedUser.getUuid(),
                authenticatedUser.getFirstName(),
                authenticatedUser.getLastName(),
                authenticatedUser.getEmail(),
                authenticatedUser.getPhoneNumber(),
                authenticatedUser.getEnabled()
        );
    }
}
