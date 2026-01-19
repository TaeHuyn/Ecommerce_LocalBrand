package fpt.com.ecommerce.order.controller;

import fpt.com.ecommerce.common.response.ApiResponse;
import fpt.com.ecommerce.order.dto.request.UpdateOrderStatusRequest;
import fpt.com.ecommerce.order.dto.response.OrderResponse;
import fpt.com.ecommerce.order.service.AdminOrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/orders")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminOrderController {

    AdminOrderService adminOrderService;

    @GetMapping
    public ApiResponse<List<OrderResponse>> getAllOrders() {
        List<OrderResponse> orders = adminOrderService.getAll();
        return ApiResponse.success("Lấy danh sách đơn hàng thành công", orders);
    }

    @PatchMapping("/{orderId}/status")
    public ApiResponse<Void> updateOrderStatus(
            @PathVariable Long orderId,
            @RequestBody UpdateOrderStatusRequest request) {
        adminOrderService.updateStatus(orderId, request.getOrderStatus());
        return ApiResponse.success("Cập nhật trạng thái đơn hàng thành công", null);
    }
}
