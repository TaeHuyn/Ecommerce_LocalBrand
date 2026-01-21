package fpt.com.ecommerce.reservation.service;

import fpt.com.ecommerce.catalog.entity.ProductVariant;
import fpt.com.ecommerce.cart.entity.Cart;
import fpt.com.ecommerce.reservation.entity.Reservation;
import fpt.com.ecommerce.order.entity.Order;

public interface ReservationService {

    Reservation getValidReservation(String reservationKey);

    Reservation createReservation(ProductVariant variant, Cart cart, int quantity, long ttlMs);

    void commitReservation(String reservationKey, Order order);
}
