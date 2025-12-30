package com.mikehenry.authentication.api.dto;

import lombok.Builder;

import java.time.Instant;

@Builder
public record LoginAttemptsResponse(
        String email,
        boolean successful,
        Instant dateCreated
) {
}
