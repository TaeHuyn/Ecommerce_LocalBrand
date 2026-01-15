package fpt.com.ecommerce.catalog.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {

    private Long id;
    private String name;
    private String category;
    private String description;
    private List<ProductVariantResponse> variants;
}
