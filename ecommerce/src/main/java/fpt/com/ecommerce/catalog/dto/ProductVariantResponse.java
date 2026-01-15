package fpt.com.ecommerce.catalog.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductVariantResponse {

    private Long id;
    private String size;
    private String color;
    private Integer price;
    private Integer stock;
}
