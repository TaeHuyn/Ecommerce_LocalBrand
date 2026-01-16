package fpt.com.ecommerce.cart.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddToCartRequest {

    private Long productVariantId;
    private Integer quantity;
}
