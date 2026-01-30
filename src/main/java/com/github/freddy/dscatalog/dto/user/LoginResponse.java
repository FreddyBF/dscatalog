package com.github.freddy.dscatalog.dto.user;

public record LoginResponse(
        String accessToken,
        String refreshToken,
        String tokenType // Geralmente "Bearer"
) {}