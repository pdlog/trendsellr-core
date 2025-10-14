package com.trendsellr.application.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.trendsellr.domain.model.user.User;
import com.trendsellr.domain.service.JwtService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService {

    private final long expirationTime;

    private final Algorithm algorithm;

    public JwtServiceImpl(
            @Value("${api.security.jwt.secret}") final String secretKey,
            @Value("${api.security.jwt.expiration-time}") final long expirationTime) {
        this.expirationTime = expirationTime;
        this.algorithm = Algorithm.HMAC256(secretKey);
    }

    @Override
    public String generateToken(final User user) {
        final long now = System.currentTimeMillis();

        return JWT.create()
                .withSubject(user.getEmail())
                .withClaim("roles", user.getRoles().stream().map(Enum::name).toList())
                .withIssuedAt(new Date(now))
                .withExpiresAt(new Date(now + this.expirationTime))
                .sign(this.algorithm);
    }


    @Override
    public String getUserIdFromToken(final String token) {
        final JWTVerifier verifier = JWT.require(this.algorithm).build();
        final DecodedJWT decodedJWT = verifier.verify(token);

        return decodedJWT.getSubject();
    }
}