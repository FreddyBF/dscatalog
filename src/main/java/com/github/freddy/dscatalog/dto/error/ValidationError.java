package com.github.freddy.dscatalog.dto.error;


import java.time.LocalDateTime;
import java.util.List;

public record ValidationError(
        LocalDateTime timestamp,
        Integer status,
        String message,
        String path,
        List<FieldMessage> fieldErrors
) { }
