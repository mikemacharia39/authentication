package com.mikehenry.authentication.api.controller;

import com.mikehenry.authentication.api.dto.LoginAttemptsResponse;
import com.mikehenry.authentication.api.dto.LoginRequest;
import com.mikehenry.authentication.api.dto.LoginResponse;
import com.mikehenry.authentication.api.dto.Problem;
import com.mikehenry.authentication.domain.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Validated
@RequestMapping("/")
@RestController
public class LoginController {
    private final LoginService loginService;

    @Operation(summary = "Authenticate user and return token")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginResponse.class)))
    @ApiResponse(responseCode = "401", content = @Content(schema = @Schema(implementation = Problem.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Problem.class)))
    @PostMapping(path = "/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<LoginResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        return new ResponseEntity<>(loginService.login(loginRequest), HttpStatus.OK);
    }

    @Operation(summary = "Get login attempts by email")
    @ApiResponse(responseCode = "200", content = @Content(schema = @Schema(implementation = LoginAttemptsResponse.class)))
    @GetMapping(path = "/login-attempts", produces = "application/json")
    public ResponseEntity<Page<LoginAttemptsResponse>> getLoginAttempts(@RequestParam String email, Pageable pageable) {
        Page<LoginAttemptsResponse> loginAttempts = loginService.getLoginAttemptsByEmail(email, pageable);
        return new ResponseEntity<>(loginAttempts, HttpStatus.OK);
    }
}
