package fpt.com.ecommerce.cart.controller;

import fpt.com.ecommerce.cart.dto.request.AddToCartRequest;
import fpt.com.ecommerce.cart.dto.response.CartResponse;
import fpt.com.ecommerce.cart.service.CartService;
import fpt.com.ecommerce.common.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {

    CartService cartService;

    @GetMapping
    public ApiResponse<CartResponse> getCart(
            @RequestHeader(value = "X-Cart-Token", required = false) String cartToken
    ) {
        return ApiResponse.success(
                "Get cart successfully",
                cartService.getOrCreateCart(cartToken)
        );
    }

    @PostMapping("/items")
    public ApiResponse<CartResponse> addToCart(
            @RequestHeader("X-Cart-Token") String cartToken,
            @RequestBody AddToCartRequest addToCartRequest
    ) {
        return ApiResponse.success(
                "Add to cart successfully",
                cartService.addToCart(cartToken, addToCartRequest)
        );
    }

    @DeleteMapping("/items/{variantId}")
    public ApiResponse<Void> removeItem(
            @RequestHeader("X-Cart-Token") String cartToken,
            @PathVariable Long variantId
    ) {
        cartService.removeItem(cartToken, variantId);
        return ApiResponse.success("Remove item from cart successfully", null);
    }

    @DeleteMapping("/clear")
    public ApiResponse<Void> clearCart(
            @RequestHeader("X-Cart-Token") String cartToken
    ) {
        cartService.clearCart(cartToken);
        return ApiResponse.success("Clear cart successfully", null);
    }
}
