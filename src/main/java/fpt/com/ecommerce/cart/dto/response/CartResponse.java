package fpt.com.ecommerce.cart.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CartResponse {

    private String cartToken;
    private Integer totalPrice;
    private List<CartItemResponse> items;
}
