package fpt.com.ecommerce.auth.repository;

import fpt.com.ecommerce.auth.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);

    // Khi logout trên mọi thiết bị
//    void deleteByAccount(Account account);
}
