package com.mikehenry.authentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponse(
        @Schema(description = "JWT token")
        String accessToken,
        @Schema(description = "Token type", example = "Bearer")
        String tokenType
) {
}
