package com.github.freddy.dscatalog.auth;


import com.github.freddy.dscatalog.security.UserDetailsImpl;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

@Component
public class JwtService {
    @Value("${api.security.token.secret}")
    private String secret;

    @Value("${api.security.token.expiration}")
    private Long expirationTime;

    @Value("${api.security.token.refresh-expiration}")
    private Long refreshTokenExpiration;

    private SecretKey key;

    @PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails user) {
        var roles = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        String userId = null;
        if (user instanceof UserDetailsImpl userImpl) {
            userId = userImpl.id().toString();
        }

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("id", userId)
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(this.key, Jwts.SIG.HS256)
                .compact();
    }

    public String generateRefreshToken(UserDetails user) {
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    // --- Métodos de Extração Pública ---

    public String getUserEmailFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }

    public String getUserIdFromToken(String token) {
        return getClaim(token, claims -> claims.get("id", String.class));
    }

    public List<String> getRolesFromToken(String token) {
        return getClaim(token, claims -> (List<String>) claims.get("roles"));
    }

    // --- Validação ---

    public boolean validateToken(String token) {
        try {
            String email = getUserEmailFromToken(token);
            return (email != null && !email.isBlank() && !isTokenExpired(token));
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    // --- Métodos Auxiliares e Privados (O "Coração" do Provider) ---

    private <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}

