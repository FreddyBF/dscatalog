package com.github.freddy.dscatalog.service;

import com.github.freddy.dscatalog.dto.PageResponse;
import com.github.freddy.dscatalog.dto.category.CategoryRequest;
import com.github.freddy.dscatalog.dto.category.CategoryResponse;
import com.github.freddy.dscatalog.exception.ConflictException;
import com.github.freddy.dscatalog.exception.ResourceNotFoundException;
import com.github.freddy.dscatalog.model.Category;
import com.github.freddy.dscatalog.repository.CategoryRepository;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    @Transactional
    public CategoryResponse createCategory(CategoryRequest category) {
        if(categoryRepository.existsByNameIgnoreCase(category.name())) {
            throw new ConflictException("Categoria já existente");
        }
        Category cat = new  Category();
        cat.setName(category.name());
        cat = categoryRepository.save(cat);
        return new CategoryResponse(cat);
    }

    @Transactional
    public CategoryResponse findCategoryById(UUID id) {
        var category = categoryRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Categoria não encontrda")
        );
        return new CategoryResponse(category);
    }

    @Transactional
    public PageResponse<CategoryResponse> findAll(Pageable pageable) {
        Page<Category> categories = categoryRepository.findAll(pageable);
        Page<CategoryResponse> dto = categories.map(CategoryResponse::new);
        return PageResponse.fromPage(dto);
    }

    @Transactional
    public CategoryResponse updateCategory(@PathVariable UUID id, CategoryRequest category) {
        Category cat = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não existe"));
        cat.setName(category.name());
        cat = categoryRepository.save(cat);
        return new CategoryResponse(cat);
    }

    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Categoria não encontrada");
        }
        categoryRepository.deleteById(id);
    }
}

