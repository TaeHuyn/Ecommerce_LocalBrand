package fpt.com.ecommerce.catalog.service.impl;

import fpt.com.ecommerce.catalog.dto.ProductDetailResponse;
import fpt.com.ecommerce.catalog.dto.ProductResponse;
import fpt.com.ecommerce.catalog.entity.Product;
import fpt.com.ecommerce.catalog.mapper.ProductMapper;
import fpt.com.ecommerce.catalog.repository.ProductRepository;
import fpt.com.ecommerce.catalog.service.ProductService;
import fpt.com.ecommerce.catalog.specification.ProductSpecification;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {

    ProductRepository productRepository;
    ProductMapper productMapper;

    public Page<ProductResponse> getProducts(
            String category,
            String keyword,
            Integer minPrice,
            Integer maxPrice,
            String sort,
            int page,
            int size
    ) {
        Sort sortObj = sort.equals("price,asc")
                ? Sort.by("minPrice").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, sortObj);

        Specification<Product> spec = Specification
                .where(ProductSpecification.hasCategory(category))
                .and(ProductSpecification.nameContains(keyword))
                .and(ProductSpecification.filterByPriceRange(minPrice, maxPrice));

        return productRepository.findAll(spec, pageable)
                .map(productMapper::toResponse);
    }

    public ProductDetailResponse getProductBySlug(String slug) {

        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponseDetail(product);
    }

    public ProductDetailResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return productMapper.toResponseDetail(product);
    }

}
