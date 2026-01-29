package com.github.freddy.dscatalog.dto.product;

import com.github.freddy.dscatalog.dto.category.CategoryResponse;
import com.github.freddy.dscatalog.model.Product;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public record ProductResponse(
        UUID id,
        String name,
        BigDecimal price,
        String description,
        String imgUrl,
        Set<CategoryResponse> categories
) {

    public ProductResponse(Product product) {
        this(
                product.getId(),
                product.getName(),
                product.getPrice(),
                product.getDescription(),
                product.getImgUrl(),
                product.getCategories().stream().map(CategoryResponse::new).collect(Collectors.toSet())
        );
    }
}