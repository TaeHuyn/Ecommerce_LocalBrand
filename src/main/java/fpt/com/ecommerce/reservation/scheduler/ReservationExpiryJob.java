package fpt.com.ecommerce.reservation.scheduler;

import fpt.com.ecommerce.reservation.repository.ReservationRepository;
import fpt.com.ecommerce.catalog.repository.ProductVariantRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import jakarta.annotation.PostConstruct;

import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class ReservationExpiryJob {

    private static final Logger log = LoggerFactory.getLogger(ReservationExpiryJob.class);

    private final ReservationRepository reservationRepository;
    private final ProductVariantRepository variantRepository;

    public ReservationExpiryJob(ReservationRepository reservationRepository,
            ProductVariantRepository variantRepository) {
        this.reservationRepository = reservationRepository;
        this.variantRepository = variantRepository;
    }

    @Scheduled(fixedDelayString = "60000")
    @Transactional
    public void expireReservations() {
        var now = Instant.now();
        System.out.println(now);
        try {
            var expired = reservationRepository.findByExpiresAtBeforeAndStatus(now, "HELD");
            if (expired.isEmpty()) {
                log.debug("No expired reservations found at {}", now);
                return;
            }

            log.info("Found {} expired reservations to process", expired.size());

            for (var r : expired) {
                try {
                    int updated = variantRepository.releaseStock(r.getVariant().getId(), r.getQuantity());
                    if (updated == 0) {
                        log.warn("Failed to release stock for reservation id={} variantId={} qty={}",
                                r.getId(), r.getVariant().getId(), r.getQuantity());
                    } else {
                        log.info("Released stock for reservation id={} variantId={} qty={}",
                                r.getId(), r.getVariant().getId(), r.getQuantity());
                    }

                    r.setStatus("EXPIRED");
                    reservationRepository.save(r);
                } catch (Exception ex) {
                    log.error("Error processing reservation id={}: {}", r.getId(), ex.getMessage(), ex);
                }
            }
        } catch (Exception e) {
            log.error("ReservationExpiryJob failed: {}", e.getMessage(), e);
        }
    }

    @PostConstruct
    public void init() {
        log.info("ReservationExpiryJob initialized; will run every 60s");
    }
}
