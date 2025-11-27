package dev.aurivena.lms.domain.account;

public record TokenPair(
        String accessToken,
        String refreshToken
) {
}
