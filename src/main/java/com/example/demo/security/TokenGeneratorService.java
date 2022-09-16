package com.example.demo.security;

import com.example.demo.dto.User;

public interface TokenGeneratorService {

    String generateToken(User user);

    String generateInternalUserToken();
}
