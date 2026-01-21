package fpt.com.ecommerce.cart.service.impl;

import fpt.com.ecommerce.cart.dto.request.AddToCartRequest;
import fpt.com.ecommerce.cart.dto.request.UpdateCartItemRequest;
import fpt.com.ecommerce.cart.dto.response.CartItemResponse;
import fpt.com.ecommerce.cart.dto.response.CartResponse;
import fpt.com.ecommerce.cart.entity.Cart;
import fpt.com.ecommerce.cart.entity.CartItem;
import fpt.com.ecommerce.cart.repository.CartRepository;
import fpt.com.ecommerce.cart.service.CartService;
import fpt.com.ecommerce.catalog.entity.ProductVariant;
import fpt.com.ecommerce.catalog.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
@Transactional
public class CartServiceImpl implements CartService {

    CartRepository cartRepository;
    ProductVariantRepository productVariantRepository;

    @Override
    public CartResponse getOrCreateCart(String cartToken) {

        Cart cart;

        if (cartToken == null || cartToken.isBlank()) {
            cart = cartRepository.save(
                    Cart.builder()
                            .cartToken(UUID.randomUUID().toString())
                            .build()
            );
        } else {
            cart = cartRepository.findByCartToken(cartToken)
                    .orElseGet(() -> cartRepository.save(
                            Cart.builder()
                                    .cartToken(cartToken)
                                    .build()
                    ));
        }

        return toResponse(cart);
    }

    @Override
    public CartResponse addToCart(String cartToken, AddToCartRequest request) {

        Cart cart = cartRepository.findByCartToken(cartToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        ProductVariant variant = productVariantRepository.findById(request.getProductVariantId())
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductVariant().getId().equals(variant.getId()))
                .findFirst()
                .orElse(null);

        if (item == null) {
            item = CartItem.builder()
                    .cart(cart)
                    .productVariant(variant)
                    .quantity(request.getQuantity())
                    .price(variant.getPrice())
                    .build();
            cart.getItems().add(item);
        } else {
            item.setQuantity(item.getQuantity() + request.getQuantity());
        }

        return toResponse(cart);
    }

    @Override
    public CartResponse updateItemQuantity(String cartToken, UpdateCartItemRequest request) {

        Cart cart = cartRepository.findByCartToken(cartToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getProductVariant().getId().equals(request.getVariantId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product variant not found"));

        ProductVariant variant = item.getProductVariant();

        item.setQuantity(request.getQuantity());

        return toResponse(cart);
    }

    @Override
    public void removeItem(String cartToken, Long variantId) {

        Cart cart = cartRepository.findByCartToken(cartToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().removeIf(i -> i.getProductVariant().getId().equals(variantId));
    }

    @Override
    public void clearCart(String cartToken) {

        Cart cart = cartRepository.findByCartToken(cartToken)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getItems().clear();
    }

    private CartResponse toResponse(Cart cart) {
        return CartResponse.builder()
                .cartToken(cart.getCartToken())
                .items(
                        cart.getItems().stream().map(item ->
                                CartItemResponse.builder()
                                        .variantId(item.getProductVariant().getId())
                                        .size(item.getProductVariant().getSize())
                                        .color(item.getProductVariant().getColor())
                                        .quantity(item.getQuantity())
                                        .price(item.getPrice())
                                        .build()
                        ).toList()
                )
                .totalPrice(
                        cart.getItems().stream()
                                .mapToInt(i -> i.getPrice() * i.getQuantity())
                                .sum()
                )
                .build();
    }
}
