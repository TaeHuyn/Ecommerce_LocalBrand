package fpt.com.ecommerce.config.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;

@Component
public class JwtUtil {

    private final Key key;
    private final long accessExpirationMs;
    private final long refreshExpirationMs;

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access.expirationMs}") long accessExpirationMs,
            @Value("${jwt.refresh.expirationMs}") long refreshExpirationMs
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.accessExpirationMs = accessExpirationMs;
        this.refreshExpirationMs = refreshExpirationMs;
    }

    public String generateAccessToken(Long userId, String username, List<String> roles) {

        Date now = new Date();

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("roles", roles)
                .claim("type", "ACCESS")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + accessExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String generateRefreshToken(Long userId, String username) {

        Date now = new Date();

        return Jwts.builder()
                .setSubject(username)
                .claim("userId", userId)
                .claim("type", "REFRESH")
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExpirationMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
