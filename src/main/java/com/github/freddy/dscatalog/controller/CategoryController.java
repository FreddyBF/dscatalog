package com.github.freddy.dscatalog.controller;


import com.github.freddy.dscatalog.dto.PageResponse;
import com.github.freddy.dscatalog.dto.category.CategoryRequest;
import com.github.freddy.dscatalog.dto.category.CategoryResponse;
import com.github.freddy.dscatalog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<CategoryResponse> category(@RequestBody @Valid CategoryRequest dto) {
        var category = categoryService.createCategory(dto);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .buildAndExpand(category.id())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> findById(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findCategoryById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<CategoryResponse>> findAl(Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(categoryService.findAll(pageable));
    }

}

