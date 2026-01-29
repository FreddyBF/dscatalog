package com.github.freddy.dscatalog.repository;

import com.github.freddy.dscatalog.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Boolean existsByNameIgnoreCase(String name);
}
