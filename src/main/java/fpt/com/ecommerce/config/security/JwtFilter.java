package fpt.com.ecommerce.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.jspecify.annotations.NullMarked;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
@NullMarked
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            try {
                String token = header.substring(7);
                Claims claims = jwtUtil.extractClaims(token);

                if (!"ACCESS".equals(claims.get("type"))) {
                    filterChain.doFilter(request, response);
                    return;
                }

                String username = claims.getSubject();
                Object rawRoles = claims.get("roles");

                if (rawRoles instanceof List<?>) {
                    List<GrantedAuthority> authorities =
                            ((List<?>) rawRoles).stream()
                                    .map(Object::toString)
                                    .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                                    .toList();

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username, null, authorities
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }

            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}

