package fpt.com.ecommerce.reservation.repository;

import fpt.com.ecommerce.reservation.entity.Reservation;
import fpt.com.ecommerce.catalog.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByExpiresAtBeforeAndStatus(Instant now, String status);

    Optional<Reservation> findByReservationKey(String key);

    @Modifying
    @Query("update Reservation r set r.status = :status where r.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") String status);

    List<Reservation> findByVariantAndStatus(ProductVariant variant, String status);
}
