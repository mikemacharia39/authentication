package com.mikehenry.authentication.domain.service;

import com.mikehenry.authentication.api.dto.LoginRequest;
import com.mikehenry.authentication.api.dto.LoginResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;

    public LoginResponse login(final LoginRequest loginRequest) {

    }
}
