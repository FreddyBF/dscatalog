package com.github.freddy.dscatalog.service;

import com.github.freddy.dscatalog.dto.product.ProductRequest;
import com.github.freddy.dscatalog.dto.product.ProductResponse;
import com.github.freddy.dscatalog.exception.ResourceNotFoundException;
import com.github.freddy.dscatalog.model.Category;
import com.github.freddy.dscatalog.model.Product;
import com.github.freddy.dscatalog.repository.CategoryRepository;
import com.github.freddy.dscatalog.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {

    @Mock
    ProductRepository productRepository;

    @Mock
    CategoryRepository categoryRepository;

    @InjectMocks
    ProductService productService;

    @Test
    void createShouldCreateProductSuccessfully() {
        // 1. DADOS DE ENTRADA (CENÁRIO)
        UUID categoryId = UUID.fromString("a272baf8-b7f0-4be2-a8c7-18dfd2e39569");
        Set<UUID> categoryIds = new HashSet<>();
        categoryIds.add(categoryId);

        Category categoryEntity = new Category();
        categoryEntity.setId(categoryId);

        ProductRequest productRequest = new ProductRequest(
                "Itel S16",
                new BigDecimal(80000),
                "O novo modelo itel",
                "https://itel.com/s16.jpg",
                categoryIds
        );

        // Simulando a entidade que o banco "salvaria"

        Product productEntity = new Product();
        productEntity.setId(UUID.randomUUID()); // O banco gera um ID
        productEntity.setName(productRequest.name());
        productEntity.getCategories().add(categoryEntity);

        // 2. COMPORTAMENTO DOS MOCKS
        // Quando buscar as categorias pelos IDs, retorne a categoria criada acima
        when(categoryRepository.findAllById(categoryIds))
                .thenReturn(List.of(categoryEntity));

        //Quando o repositório salvar QUALQUER produto, retorne a entidade simulada
        when(productRepository.save(ArgumentMatchers.any(Product.class)))
                .thenReturn(productEntity);

        // 3. EXECUÇÃO
        ProductResponse savedProduct = productService.create(productRequest);

        // 4. ASSERÇÕES
        Assertions.assertNotNull(savedProduct);
        Assertions.assertNotNull(savedProduct.id()); // Verifica se tem ID
        Assertions.assertEquals(productRequest.name(), savedProduct.name());

        // Verifica se a categoria está presente na resposta
        Assertions.assertFalse(savedProduct.categories().isEmpty());
    }

    @Test
    void createShouldThrowExceptionWhenCategoryDoesNotExist() {
        // ARRANGE
        UUID invalidId = UUID.randomUUID();
        ProductRequest request = new ProductRequest(
                "Erro", new BigDecimal(10), "...", "...", Set.of(invalidId)
        );

        // MOCK: Simula que o banco NÃO encontrou nenhuma categoria
        when(categoryRepository.findAllById(anySet())).thenReturn(Collections.emptyList());

        // ACT & ASSERT
        // Aqui verificamos se o teu Service lança uma exceção personalizada (ex: ResourceNotFoundException)
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            productService.create(request);
        });
    }
}
