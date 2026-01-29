package com.github.freddy.dscatalog.controller;


import com.github.freddy.dscatalog.dto.PageResponse;
import com.github.freddy.dscatalog.dto.product.ProductMinResponse;
import com.github.freddy.dscatalog.dto.product.ProductRequest;
import com.github.freddy.dscatalog.dto.product.ProductResponse;
import com.github.freddy.dscatalog.service.ProductService;
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

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> addProduct(@RequestBody @Valid ProductRequest product) {
        var productResponse = productService.create(product);
        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/products/{id}")
                .buildAndExpand(productResponse.id())
                .toUri();
        return ResponseEntity.created(location).body(productResponse);
    }


    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID id) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageResponse<ProductMinResponse>> getAllProducts(
            @RequestParam(value = "categoryId", required = false) UUID categoryId,
            Pageable pageable
    ) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.findAll(categoryId, pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequest product) {
        return ResponseEntity.status(HttpStatus.OK).body(productService.update(id, product));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable UUID id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

