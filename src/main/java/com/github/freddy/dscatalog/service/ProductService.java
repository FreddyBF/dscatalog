package com.github.freddy.dscatalog.service;


import com.github.freddy.dscatalog.dto.PageResponse;
import com.github.freddy.dscatalog.dto.product.ProductMinResponse;
import com.github.freddy.dscatalog.dto.product.ProductRequest;
import com.github.freddy.dscatalog.dto.product.ProductResponse;
import com.github.freddy.dscatalog.exception.ResourceNotFoundException;
import com.github.freddy.dscatalog.model.Category;
import com.github.freddy.dscatalog.model.Product;
import com.github.freddy.dscatalog.repository.CategoryRepository;
import com.github.freddy.dscatalog.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProductResponse create(ProductRequest product) {
        var newProduct = new Product();
        updateProductData(newProduct, product);

        List<Category> categories = categoryRepository.findAllById(product.categoriesIds());
        if (categories.size() != product.categoriesIds().size()) {
            throw new ResourceNotFoundException("Uma ou mais categorias não foram encontradas");
        }
        newProduct.getCategories().addAll(categories);

        return new ProductResponse(productRepository.save(newProduct));
    }

    @Transactional
    public PageResponse<ProductMinResponse> findAll(UUID categoryId, Pageable pageable) {
        Page<Product> page;

        if (categoryId != null) {
            page = productRepository.findByCategoriesId(categoryId, pageable);
        } else {
            page = productRepository.findAll(pageable);
        }
        return PageResponse.fromPage(productRepository.findAll(pageable).map(ProductMinResponse::new));
    }

    @Transactional
    public ProductResponse findById(UUID id) {
        var product = productRepository.findByIdWithCategories(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        return new ProductResponse(product);
    }

    @Transactional
    public ProductResponse update(UUID id, ProductRequest dto) {
        var product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado"));
        updateProductData(product, dto);

        if (dto.categoriesIds() != null) {
            product.getCategories().clear();
            List<Category> categories = categoryRepository.findAllById(dto.categoriesIds());
            product.getCategories().addAll(categories);
        }
        return new ProductResponse(productRepository.save(product));
    }

    @Transactional
    public void delete(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Produto não encontrado");
        }
        //Remove os vínculos na tabela de junção via SQL puro
        productRepository.deleteAssociations(id);
        productRepository.deleteById(id);
    }

    private void updateProductData(Product entity, ProductRequest dto) {
        entity.setName(dto.name());
        entity.setPrice(dto.price());
        entity.setDescription(dto.description());
        entity.setImgUrl(dto.imgUrl());
    }
}
