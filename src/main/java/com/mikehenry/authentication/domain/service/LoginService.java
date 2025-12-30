package com.mikehenry.authentication.domain.service;

import com.mikehenry.authentication.api.dto.LoginAttemptsResponse;
import com.mikehenry.authentication.api.dto.LoginRequest;
import com.mikehenry.authentication.api.dto.LoginResponse;
import com.mikehenry.authentication.domain.entity.LoginAttempt;
import com.mikehenry.authentication.domain.repository.LoginAttemptRepository;
import com.mikehenry.authentication.domain.util.JwtHelper;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class LoginService {
    private final AuthenticationManager authenticationManager;
    private final LoginAttemptRepository loginAttemptRepository;

    public LoginResponse login(@NonNull final LoginRequest loginRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password()));
        } catch (BadCredentialsException e) {
            addLoginAttempt(loginRequest.email(), false);
            throw e;
        }

        final String token = JwtHelper.generateToken(loginRequest.email());
        addLoginAttempt(loginRequest.email(), true);

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType("Bearer")
                .build();
    }

    @Transactional
    public void addLoginAttempt(final String email, final boolean success) {
        LoginAttempt loginAttempt = LoginAttempt.builder()
                .email(email)
                .successful(success)
                .build();
        loginAttemptRepository.save(loginAttempt);
    }

    public Page<LoginAttemptsResponse> getLoginAttemptsByEmail(final String email, final Pageable pageable) {
        Page<LoginAttempt> loginAttempts = loginAttemptRepository.findByEmailOrderByAttemptedAtDesc(email, pageable);
        return loginAttempts.map(attempt -> new LoginAttemptsResponse(
                attempt.getEmail(),
                attempt.isSuccessful(),
                attempt.getDateCreated()
        ));
    }
}
