package fpt.com.ecommerce.catalog.repository;

import fpt.com.ecommerce.catalog.entity.Product;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

    @EntityGraph(attributePaths = {
        "variants"
    })
    Optional<Product> findBySlug(String slug);
}
