package com.payment.user.service.jwt;

import com.payment.user.common.config.JwtPropsConfig;
import com.payment.user.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JWTServiceImpl implements JWTService {

    private final JwtPropsConfig jwtConfig;

    @Override
    public String getUsernameFromToken(String token) {
        return parseClaims(token).getSubject();
    }

    @Override
    public <T> T getClaimFromToken(String token, Function<Claims, T> clazz) {
        return clazz.apply(parseClaims(token));
    }

    @Override
    public String generateToken(UserDetails details) {
        return generateToken(new HashMap<>(), details);
    }

    @Override
    public String generateToken(Map<String, Object> claims, UserDetails details) {
        return buildToken(claims, details, jwtConfig.getExpirationTime());
    }

    @Override
    public Long getExpirationTime() {
        return jwtConfig.getExpirationTime();
    }

    private String buildToken(Map<String, Object> claims, UserDetails details, long expiresIn) {
        return Jwts.builder().setClaims(claims).setSubject(details.getUsername()).setIssuedAt(new Date(System.currentTimeMillis())).setExpiration(new Date(System.currentTimeMillis() + expiresIn)).signWith(getKey(), SignatureAlgorithm.HS256).compact();
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return username != null && username.equals(userDetails.getUsername()) && validateExpiration(token);
    }

    public Boolean validateExpiration(String token) {
        return parseClaims(token).getExpiration().before(new Date());
    }


    private Claims parseClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    public Key getKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtConfig.getSecretKey());
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
