package com.github.freddy.dscatalog.service;

import com.github.freddy.dscatalog.dto.category.CategoryRequest;
import com.github.freddy.dscatalog.dto.category.CategoryResponse;
import com.github.freddy.dscatalog.model.Category;
import com.github.freddy.dscatalog.repository.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTests {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @Test
    void saveCategoryShouldSaveCategorySuccessfully() {

        CategoryRequest request = new CategoryRequest("Eletrónicos");
        Category entity = new Category();
        entity.setId(UUID.randomUUID());
        entity.setName("Eletrónicos");

        // 2. Mocks
        //Use 'any()' porque o objeto passado para o save não é exatamente o mesmo da resposta
        Mockito.when(categoryRepository.save(Mockito.any(Category.class)))
                .thenReturn(entity);

        // 3. Execução
        CategoryResponse response = categoryService.createCategory(request);

        // 4. Asserção
        Assertions.assertNotNull(response.id());
        Assertions.assertEquals("Eletrónicos", response.name());
        Mockito.verify(categoryRepository, Mockito.times(1)).save(Mockito.any(Category.class));
    }
}
