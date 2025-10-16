package com.pcparts.shop.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key secretKey;
    private final long expirationMillis;

    public JwtTokenProvider(
            @Value("${pcparts.app.jwt.secret}") String secret,
            @Value("${pcparts.app.jwt.expiration}") long expirationMillis
    ) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.expirationMillis = expirationMillis;
    }

    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
        return true;
    }
}


