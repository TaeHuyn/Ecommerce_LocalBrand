package fpt.com.ecommerce.order.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
public class CheckoutInitResponse {

    private List<String> reservationKeys;
    private Instant expiresAt;
    private Integer totalAmount;
}
