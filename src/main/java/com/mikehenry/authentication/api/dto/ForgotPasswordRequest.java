package com.mikehenry.authentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ForgotPasswordRequest(
        @Schema(description = "email", example = "mikehenry@gmail.com")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email
) {
}
