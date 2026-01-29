package com.github.freddy.dscatalog.dto.product;

import com.github.freddy.dscatalog.model.Product;
import java.math.BigDecimal;
import java.util.UUID;

public record ProductMinResponse(
        UUID id,
        String name,
        BigDecimal price,
        String imgUrl
) {
    // Construtor para converter da entidade para o DTO resumido
    public ProductMinResponse(Product entity) {
        this(
                entity.getId(),
                entity.getName(),
                entity.getPrice(),
                entity.getImgUrl()
        );
    }
}