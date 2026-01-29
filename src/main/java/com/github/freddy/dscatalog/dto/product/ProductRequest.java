package com.github.freddy.dscatalog.dto.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record ProductRequest(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @Positive(message = "O preço deve ser positivo")
        BigDecimal price,

        String description,

        @URL(message = "Url invaida")
        String imgUrl,

        @NotEmpty(message = "O produto deve ter pelo menos uma categoria")
        Set<UUID> categoriesIds
) {
}
