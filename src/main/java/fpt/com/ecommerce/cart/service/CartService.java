package fpt.com.ecommerce.cart.service;

import fpt.com.ecommerce.cart.dto.request.AddToCartRequest;
import fpt.com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import fpt.com.ecommerce.cart.dto.response.CartResponse;

public interface CartService {

    CartResponse getOrCreateCart(String cartToken);

    CartResponse addToCart(String cartToken, AddToCartRequest request);

    CartResponse updateItemQuantity(String cartToken, UpdateCartItemRequest request);

    void removeItem(String cartToken, Long variantId);

    void clearCart(String cartToken);
}
