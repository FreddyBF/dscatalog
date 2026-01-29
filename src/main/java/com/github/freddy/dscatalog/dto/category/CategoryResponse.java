package com.github.freddy.dscatalog.dto.category;

import com.github.freddy.dscatalog.model.Category;

import java.util.UUID;

public record CategoryResponse(
        UUID id,
        String name
) {
    public  CategoryResponse(Category category) {
        this(category.getId(), category.getName());
    }
}
