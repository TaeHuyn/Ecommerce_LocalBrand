package fpt.com.ecommerce.catalog.service;

import fpt.com.ecommerce.catalog.dto.ProductResponse;
import fpt.com.ecommerce.catalog.dto.ProductVariantResponse;
import fpt.com.ecommerce.catalog.entity.Product;
import fpt.com.ecommerce.catalog.repository.ProductRepository;
import fpt.com.ecommerce.catalog.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

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
                .map(this::toResponse);
    }

    public ProductResponse getProductBySlug(String slug) {

        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    public ProductResponse getProductById(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return toResponse(product);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .category(product.getCategory())
                .description(product.getDescription())
                .variants(
                        product.getVariants().stream().map(
                                v -> ProductVariantResponse.builder()
                                        .id(v.getId())
                                        .size(v.getSize())
                                        .color(v.getColor())
                                        .price(v.getPrice())
                                        .stock(v.getStock())
                                        .build())
                                .collect(Collectors.toList())
                )
                .build();
    }
}
