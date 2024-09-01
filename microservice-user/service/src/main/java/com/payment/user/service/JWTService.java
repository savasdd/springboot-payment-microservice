package com.payment.user.service;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Map;
import java.util.function.Function;

public interface JWTService {

    String getUsernameFromToken(String token);

    <T> T getClaimFromToken(String token, Function<Claims, T> clazz);

    String generateToken(UserDetails details);

    String generateToken(Map<String, Object> claims, UserDetails details);

    Long getExpirationTime();

     Boolean validateToken(String token, UserDetails userDetails);

    Boolean validateExpiration(String token);

}
