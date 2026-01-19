package fpt.com.ecommerce.catalog.dto;

import fpt.com.ecommerce.common.enums.ProductStatus;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailResponse {

    private Long id;
    private String name;
    private String slug;
    private String category;
    private String description;
    private String thumbnailUrl;

    private Integer minPrice;
    private Integer maxPrice;

    private ProductStatus status;

    private List<ProductVariantResponse> variants;
}
