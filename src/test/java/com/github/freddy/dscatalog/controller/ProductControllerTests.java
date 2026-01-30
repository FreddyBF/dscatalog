package com.github.freddy.dscatalog.controller;

import com.github.freddy.dscatalog.auth.JwtService;
import com.github.freddy.dscatalog.dto.category.CategoryResponse;
import com.github.freddy.dscatalog.dto.product.ProductRequest;
import com.github.freddy.dscatalog.dto.product.ProductResponse;
import com.github.freddy.dscatalog.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class) // 1. Carrega só o Controller
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc; // Ferramenta para fazer requisições HTTP falsas

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper; // Para converter Objetos Java <-> JSON

    @MockitoBean
    private JwtService jwtService;

    @Test
    @WithMockUser(roles = "ADMIN")// 1. Simula um usuário autenticado (evita o 403 de falta de login)
    void insertShouldReturnCreatedStatus() throws Exception {
        // ARRANGE
        UUID categoryId = UUID.randomUUID();
        ProductRequest request = new ProductRequest(
                "Celular",
                new BigDecimal(10000.0),
                "O mais novo modelo RedMi",
                "https://products/images/redmi.jpg",
                Set.of(categoryId)
                );
        UUID id = UUID.randomUUID();

        ProductResponse response = new ProductResponse(
                id, "Celular",
                new BigDecimal(10000.0),
                "O mais novo modelo RedMi",
                "https://products/images/redmi.jpg",
                Set.of(new CategoryResponse(categoryId, "Electronicos"))
        );

        when(productService.create(any())).thenReturn(response);

        // Converter o objeto java para JSON (String)
        String jsonBody = objectMapper.writeValueAsString(request);

        // ACT & ASSERT (Tudo junto usando o MockMvc)
        mockMvc.perform(post("/api/v1/products")
                        .with(csrf())
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isCreated()) // Espera código 201
                        .andExpect(
                                jsonPath("$.id").exists()
                        ) // Verifica se no JSON tem campo "id"
                        .andExpect(
                                jsonPath("$.name").value("Celular")
                        );
    }
}
