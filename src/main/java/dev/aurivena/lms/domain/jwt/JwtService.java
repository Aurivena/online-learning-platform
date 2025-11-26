package dev.aurivena.lms.domain.jwt;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private static final long ACCESS_TOKEN_VALIDITY = 15 * 60 * 1000;

    private static final long REFRESH_TOKEN_VALIDITY = 7L * 24 * 60 * 60 * 1000;

    private Algorithm getAlgorithm() {
        return Algorithm.HMAC256(secret);
    }

    public String get(JwtType jwtType, String email, String role) {
        long duration = switch (jwtType) {
            case ACCESS -> ACCESS_TOKEN_VALIDITY;
            case REFRESH -> REFRESH_TOKEN_VALIDITY;
        };

        return generateToken(duration, email, role);
    }

    private String generateToken(long duration, String email, String role) {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("email", email)
                .withClaim("role", role)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + duration))
                .withIssuer("aurivena-lms")
                .sign(getAlgorithm());
    }
}
