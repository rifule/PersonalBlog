package com.blog.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {

    private static final String SECRET = "blog-secret-key-2024";
    private static final long EXPIRATION = 2592000000L; // 30天（一个月）

    public static String generateToken(Long userId, String username, String role) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + EXPIRATION);

        Map<String, Object> header = new HashMap<>();
        header.put("alg", "HS256");
        header.put("typ", "JWT");

        return JWT.create()
                .withHeader(header)
                .withClaim("userId", userId)
                .withClaim("username", username)
                .withClaim("role", role)
                .withIssuedAt(now)
                .withExpiresAt(expiration)
                .sign(Algorithm.HMAC256(SECRET));
    }

    public static DecodedJWT parseToken(String token) {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        return verifier.verify(token);
    }

    public static Long getUserId(String token) {
        DecodedJWT jwt = parseToken(token);
        return jwt.getClaim("userId").asLong();
    }

    public static String getUsername(String token) {
        DecodedJWT jwt = parseToken(token);
        return jwt.getClaim("username").asString();
    }

    public static String getRole(String token) {
        DecodedJWT jwt = parseToken(token);
        return jwt.getClaim("role").asString();
    }
}
