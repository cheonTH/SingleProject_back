package com.single.map.security;

import io.jsonwebtoken.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.single.map.model.UserEntity;

import java.util.Date;

@Service
public class TokenProvider {
	@Value("${jwt.secret}")
    private String secretKey;
	
    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 1일

    public String create(UserEntity user) {
        return Jwts.builder()
                .setSubject(user.getUserId())
                .claim("id", user.getId())
                .claim("nickname", user.getNickName())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }
    
    public boolean validate(String token) {
        try {
            Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public String getUserId(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
