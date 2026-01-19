package fpt.com.ecommerce.order.mapper;

import fpt.com.ecommerce.order.dto.response.OrderItemResponse;
import fpt.com.ecommerce.order.dto.response.OrderResponse;
import fpt.com.ecommerce.order.entity.Order;
import fpt.com.ecommerce.order.entity.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "trackingLink",
            expression = "java(\"/api/orders/\" + order.getOrderCode())")
    OrderResponse toResponse(Order order);

    @Mapping(target = "variantId", source = "productVariant.id")
    @Mapping(target = "productName", source = "productVariant.product.name")
    @Mapping(target = "size", source = "productVariant.size")
    @Mapping(target = "color", source = "productVariant.color")
    OrderItemResponse toItemResponse(OrderItem orderItem);

    List<OrderItemResponse> toItemResponses(List<OrderItem> items);
}
