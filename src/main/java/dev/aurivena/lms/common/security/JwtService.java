package dev.aurivena.lms.common.security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.aurivena.lms.domain.account.JwtType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;


@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;


    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }


    public String generate(JwtType jwtType, String email, String role) {
        long duration = jwtType.getDuration();

        Date expirationDate = new Date(System.currentTimeMillis() + duration);

        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .withIssuer("aurivena-lms")
                .sign(getAlgorithm());
    }

    public DecodedJWT verify(String token) {
        return JWT.require(getAlgorithm())
                .build()
                .verify(token);
    }
}
