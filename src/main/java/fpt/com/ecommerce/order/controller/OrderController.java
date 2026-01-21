package fpt.com.ecommerce.order.controller;

import fpt.com.ecommerce.common.response.ApiResponse;
import fpt.com.ecommerce.order.dto.request.CheckoutConfirmRequest;
import fpt.com.ecommerce.order.dto.response.CheckoutInitResponse;
import fpt.com.ecommerce.order.dto.response.OrderResponse;
import fpt.com.ecommerce.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/checkout/init")
    public ApiResponse<CheckoutInitResponse> initCheckout(
            @RequestHeader("X-Cart-Token") String cartToken
    ) {
        return ApiResponse.success(
                "Stock reserved",
                orderService.initCheckout(cartToken)
        );
    }

    @PostMapping("/checkout/confirm")
    public ApiResponse<OrderResponse> confirmCheckout(
            @RequestBody CheckoutConfirmRequest request
    ) {
        return ApiResponse.success(
                "Order placed successfully",
                orderService.confirmCheckout(request)
        );
    }

    @GetMapping("/{orderCode}")
    public ApiResponse<OrderResponse> getOrderByCode(@PathVariable String orderCode) {
        return ApiResponse.success(
                "Order retrieved successfully",
                orderService.getByOrderCode(orderCode)
        );
    }
}
