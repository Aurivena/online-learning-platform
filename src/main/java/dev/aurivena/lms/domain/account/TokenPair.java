package dev.aurivena.lms.domain.account;

record TokenPair(
        String accessToken,
        String refreshToken
) {
}
