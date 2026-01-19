package fpt.com.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderItemResponse {

    private Long variantId;
    private String productName;
    private String size;
    private String color;
    private Integer quantity;
    private Integer price;
}
