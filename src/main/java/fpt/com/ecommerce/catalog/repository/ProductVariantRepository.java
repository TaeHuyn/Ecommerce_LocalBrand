package fpt.com.ecommerce.catalog.repository;

import fpt.com.ecommerce.catalog.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {
}
