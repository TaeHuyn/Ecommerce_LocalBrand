package fpt.com.ecommerce.catalog.repository;

import fpt.com.ecommerce.catalog.entity.ProductVariant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductVariantRepository extends JpaRepository<ProductVariant, Long> {

    @Modifying
    @Query("""
        UPDATE ProductVariant v
        SET v.reservedStock = v.reservedStock + :qty
        WHERE v.id = :id
          AND (v.stock - v.reservedStock) >= :qty
    """)
    int reserveStock(@Param("id") Long id, @Param("qty") int qty);

    @Modifying
    @Query("""
        UPDATE ProductVariant v
        SET v.reservedStock = v.reservedStock + :qty
        WHERE v.id = :id
          AND (v.stock - v.reservedStock) >= :qty
    """)
    void releaseStock(@Param("id") Long id, @Param("qty") int qty);

    @Modifying
    @Query("""
        UPDATE ProductVariant v
        SET v.reservedStock = v.reservedStock - :qty
        WHERE v.id = :id
    """)
    void confirmStock(@Param("id") Long id, @Param("qty") int qty);

}
