package fpt.com.ecommerce.auth.controller;

import fpt.com.ecommerce.auth.dto.LoginRequest;
import fpt.com.ecommerce.auth.entity.Account;
import fpt.com.ecommerce.auth.entity.RefreshToken;
import fpt.com.ecommerce.auth.repository.AccountRepository;
import fpt.com.ecommerce.auth.repository.RefreshTokenRepository;
import fpt.com.ecommerce.config.security.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountController {

    AuthenticationManager authenticationManager;
    AccountRepository accountRepository;
    RefreshTokenRepository refreshTokenRepository;
    JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestBody LoginRequest loginRequest,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        Account admin = accountRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        List<String> roles = List.of(admin.getRoles().split(","));

        String accessToken = jwtUtil.generateAccessToken(
                admin.getId(),
                admin.getUsername(),
                roles
        );

        String refreshTokenValue = jwtUtil.generateRefreshToken(
                admin.getId(),
                admin.getUsername()
        );

        String userAgent = request.getHeader("User-Agent");

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenValue)
                .account(admin)
                .expiresAt(Instant.now().plusSeconds(7 * 24 * 60 * 60))
                .revoked(false)
                .deviceInfo(userAgent)
                .build();

        refreshTokenRepository.save(refreshToken);

        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshTokenValue)
                .httpOnly(true)
                .secure(true)
                .path("/api/admin/auth")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(
                Map.of("accessToken", accessToken)
        );
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenValue
    ) {
        if (refreshTokenValue == null) {
            return ResponseEntity.status(401).build();
        }

        var claims = jwtUtil.extractClaims(refreshTokenValue);

        if (!"REFRESH".equals(claims.get("type"))) {
            return ResponseEntity.status(401).build();
        }

        RefreshToken refreshToken = refreshTokenRepository
                .findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (refreshToken.isRevoked() ||
                refreshToken.getExpiresAt().isBefore(Instant.now())) {
            return ResponseEntity.status(401).build();
        }

        Account admin = refreshToken.getAccount();
        List<String> roles = List.of(admin.getRoles().split(","));

        String newAccessToken = jwtUtil.generateAccessToken(
                admin.getId(),
                admin.getUsername(),
                roles
        );

        return ResponseEntity.ok(
                Map.of("accessToken", newAccessToken)
        );
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(
            @CookieValue(name = "refreshToken", required = false) String refreshTokenValue,
            HttpServletResponse response
    ) {
        if (refreshTokenValue != null) {
            refreshTokenRepository.findByToken(refreshTokenValue)
                    .ifPresent(token -> {
                        token.setRevoked(true);
                        refreshTokenRepository.save(token);
                    });
        }

        ResponseCookie cookie = ResponseCookie.from("refreshToken", "")
                .path("/api/admin/auth")
                .maxAge(0)
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        return ResponseEntity.ok(
                Map.of("message", "Logged out successfully")
        );
    }
}
