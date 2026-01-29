package com.github.freddy.dscatalog.repository;

import com.github.freddy.dscatalog.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Modifying
    @Query(value = "DELETE FROM tb_product_category WHERE product_id = :id", nativeQuery = true)
    void deleteAssociations(UUID id);

    Page<Product> findByCategoriesId(UUID categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p JOIN FETCH p.categories WHERE p.id = :id")
    Optional<Product> findByIdWithCategories(UUID id);
}
