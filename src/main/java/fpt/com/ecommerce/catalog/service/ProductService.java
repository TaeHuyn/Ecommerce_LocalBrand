package fpt.com.ecommerce.catalog.service;

import fpt.com.ecommerce.catalog.dto.ProductDetailResponse;
import fpt.com.ecommerce.catalog.dto.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

     Page<ProductResponse> getProducts(
            String category,
            String keyword,
            Integer minPrice,
            Integer maxPrice,
            String sort,
            int page,
            int size
    );

    ProductDetailResponse getProductBySlug(String slug);

    ProductDetailResponse getProductById(Long id);
}
