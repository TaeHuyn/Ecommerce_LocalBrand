package fpt.com.ecommerce.reservation.service.impl;

import fpt.com.ecommerce.catalog.entity.ProductVariant;
import fpt.com.ecommerce.cart.entity.Cart;
import fpt.com.ecommerce.reservation.entity.Reservation;
import fpt.com.ecommerce.reservation.repository.ReservationRepository;
import fpt.com.ecommerce.catalog.repository.ProductVariantRepository;
import fpt.com.ecommerce.reservation.service.ReservationService;
import fpt.com.ecommerce.order.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ReservationServiceImpl implements ReservationService {

    ReservationRepository reservationRepository;
    ProductVariantRepository variantRepository;

    @Override
    public Reservation getValidReservation(String reservationKey) {

        Reservation reservation = reservationRepository
                .findByReservationKey(reservationKey)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));

        if (!"HELD".equals(reservation.getStatus())) {
            throw new RuntimeException("Reservation is not valid");
        }

        if (reservation.getExpiresAt().isBefore(Instant.now())) {
            throw new RuntimeException("Reservation has expired");
        }

        return reservation;
    }

    @Override
    @Transactional
    public Reservation createReservation(ProductVariant variant, Cart cart, int quantity, long ttlMs) {

        int updated = variantRepository.reserveStock(variant.getId(), quantity);
        if (updated == 0) {
            throw new RuntimeException("Out of stock");
        }

        Reservation r = Reservation.builder()
                .variant(variant)
                .cart(cart)
                .quantity(quantity)
                .expiresAt(Instant.now().plusMillis(ttlMs))
                .status("HELD")
                .reservationKey(UUID.randomUUID().toString())
                .build();

        return reservationRepository.save(r);
    }

    @Override
    @Transactional
    public void commitReservation(String reservationKey, Order order) {

        reservationRepository.findByReservationKey(reservationKey).ifPresent(r -> {
            int updated = variantRepository.confirmStock(r.getVariant().getId(), r.getQuantity());
            if (updated == 0) {
                throw new RuntimeException("Failed to confirm stock for reservation " + reservationKey);
            }
            r.setStatus("COMMITTED");
            r.setOrder(order);
            reservationRepository.save(r);
        });
    }
}
