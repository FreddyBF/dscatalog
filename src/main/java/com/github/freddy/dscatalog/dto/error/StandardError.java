package com.github.freddy.dscatalog.dto.error;

import java.time.LocalDateTime;

public record StandardError(
        LocalDateTime timestamp,
        Integer status,
        String message,
        String path
) {
}
