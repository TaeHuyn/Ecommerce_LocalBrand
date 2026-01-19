package fpt.com.ecommerce.order.controller;

import fpt.com.ecommerce.common.response.ApiResponse;
import fpt.com.ecommerce.order.dto.request.CheckoutRequest;
import fpt.com.ecommerce.order.dto.response.OrderResponse;
import fpt.com.ecommerce.order.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderController {

    OrderService orderService;

    @PostMapping("/checkout")
    public ApiResponse<OrderResponse> checkout(
            @RequestHeader("X-Cart-Token") String cartToken,
            @RequestBody CheckoutRequest checkoutRequest
    ) {

        return ApiResponse.success(
                "Cảm ơn bạn đã đặt hàng! Đơn hàng của bạn đang được xử lý.",
                orderService.checkout(cartToken, checkoutRequest)
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
