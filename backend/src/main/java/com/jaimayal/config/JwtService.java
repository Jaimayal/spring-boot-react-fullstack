package com.jaimayal.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.issuer}")
    private String issuer;
    
    @Value("${jwt.expiration-seconds}")
    private Integer expirationSeconds;

    public String issueToken(String subject) {
        return issueToken(subject, Map.of());
    }

    public String issueToken(String subject, String... scopes) {
        return issueToken(subject, Map.of("scopes", scopes));
    }
    
    public String issueToken(String subject, Map<String, Object> claims) {
        return Jwts.builder()
                .addClaims(claims)
                .setSubject(subject)
                .setIssuer(this.issuer)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(this.expirationSeconds)))
                .signWith(this.getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
    
    public String getSubject(String token) {
        return this.getClaims(token).getOrDefault("sub", "").toString();
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(this.getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public boolean isValid(String token, String username) {
        return this.getSubject(token).equals(username) && !this.isExpired(token);
    }
    
    public boolean isExpired(String token) {
        return this.getClaims(token).getExpiration().before(Date.from(Instant.now()));
    }
}
