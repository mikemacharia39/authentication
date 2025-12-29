package com.mikehenry.authentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record InvitationResponse(
        @Schema(description = "email", example = "mikehenry@gmail.com")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,

        @Schema(description = "invitation code", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "Invitation code cannot be blank")
        String invitationCode,

        @Schema(description = "message", example = "Invitation sent successfully. Use the invitation code to reset your password.")
        String message
) {
}
