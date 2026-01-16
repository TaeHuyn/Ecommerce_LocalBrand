package fpt.com.ecommerce.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CartItemResponse {

    private Long variantId;
    private String size;
    private String color;
    private Integer quantity;
    private Integer price;
}
