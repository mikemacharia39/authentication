package com.mikehenry.authentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record ResetPasswordRequest(
        @Schema(description = "email", example = "mikehenry@gmail.com")
        @Email(message = "Invalid email format")
        @NotBlank(message = "Email cannot be blank")
        String email,
        @Schema(description = "new password", example = "NewPassw0rd!")
        @NotBlank(message = "New password cannot be blank")
        String newPassword,
        @Schema(description = "This is the invitation code or reset password code sent via email", example = "123e4567-e89b-12d3-a456-426614174000")
        @NotBlank(message = "Reset code cannot be blank")
        String resetCode
) {
}
