package com.mikehenry.authentication.api.controller;

import com.mikehenry.authentication.api.dto.ForgotPasswordRequest;
import com.mikehenry.authentication.api.dto.InvitationRequest;
import com.mikehenry.authentication.api.dto.InvitationResponse;
import com.mikehenry.authentication.api.dto.Problem;
import com.mikehenry.authentication.api.dto.ResetPasswordRequest;
import com.mikehenry.authentication.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/users")
@Validated
@RestController
public class UserController {

    private final UserService userService;

    @Operation(summary = "Invite a new user")
    @ApiResponse(responseCode = "201", content = @Content(schema = @Schema(implementation = InvitationResponse.class)))
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Problem.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Problem.class)))
    @PostMapping(path = "/invite", produces = "application/json", consumes = "application/json")
    public ResponseEntity<InvitationResponse> inviteUser(@RequestBody @Valid InvitationRequest invitationRequest) {
        return new ResponseEntity<>(userService.inviteUser(invitationRequest), HttpStatus.CREATED);
    }

    @Operation(summary = "Forgot password")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Problem.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Problem.class)))
    @PostMapping(path = "/forgot-password", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> forgotPassword(@RequestBody @Valid ForgotPasswordRequest forgotPasswordRequest) {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Reset password")
    @ApiResponse(responseCode = "200")
    @ApiResponse(responseCode = "403", content = @Content(schema = @Schema(implementation = Problem.class)))
    @ApiResponse(responseCode = "404", content = @Content(schema = @Schema(implementation = Problem.class)))
    @PostMapping(path = "/reset-password", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Void> resetPassword(@RequestBody @Valid ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(resetPasswordRequest);
        return ResponseEntity.ok().build();
    }
}
