package fpt.com.ecommerce.catalog.controller;

import fpt.com.ecommerce.catalog.dto.ProductDetailResponse;
import fpt.com.ecommerce.catalog.dto.ProductResponse;
import fpt.com.ecommerce.catalog.service.impl.ProductServiceImpl;
import fpt.com.ecommerce.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductServiceImpl productService;

    @GetMapping
    public ApiResponse<Page<ProductResponse>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer minPrice,
            @RequestParam(required = false) Integer maxPrice,
            @RequestParam(defaultValue = "price,asc") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "6") int size
    ) {

        if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
            throw new IllegalArgumentException("minPrice must be less than maxPrice");
        }

        return ApiResponse.success(
                "Products retrieved successfully",
                productService.getProducts(category, keyword, minPrice, maxPrice, sort, page, size)
        );
    }

    @GetMapping("/{slug}")
    public ApiResponse<ProductDetailResponse> getProductDetail(@PathVariable String slug) {
        return ApiResponse.success(
                "Product retrieved successfully",
                productService.getProductBySlug(slug)
        );
    }
}
