package com.mikehenry.authentication.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record Problem(
        @Schema(description = "Error code")
        int errorCode,
        @Schema(description = "Error message")
        String message) {
}
