package fpt.com.ecommerce.order.dto.request;

import fpt.com.ecommerce.common.enums.OrderStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UpdateOrderStatusRequest {

    private OrderStatus orderStatus;
}
