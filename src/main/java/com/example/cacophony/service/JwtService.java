package com.example.cacophony.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

public interface JwtService {
    public String generateToken(String userName);

    public String extractUsername(String token);

    public Date extractExpiration(String token);

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

    public Boolean validateToken(String token, UserDetails userDetails);

}
