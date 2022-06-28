package com.example.demo.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Collection;
import java.util.Collections;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
public class User extends org.springframework.security.core.userdetails.User {

    private String uuid;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private Boolean enabled;

    public User() {
        super(" ", "", Collections.emptyList());
    }

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
    }

    public User(String username, String password, Collection<? extends GrantedAuthority> authorities,
                String uuid, String firstName, String lastName, String email, String phoneNumber, Boolean enabled) {
        super(username, password, authorities);
        this.uuid = uuid;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.enabled = enabled;
    }

    @Override
    @JsonDeserialize(using = GrantedAuthorityDeserializer.class)
    public Collection<GrantedAuthority> getAuthorities() {
        return super.getAuthorities();
    }

    @Override
    public String toString() {
        return "User{" +
                "uuid='" + uuid + '\'' +
                ", username='" + username + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", enabled=" + enabled +
                ", authorities=" + getAuthorities() +
                '}';
    }
}
