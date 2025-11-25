package dev.aurivena.lms.domain.account.dto;

public record AuthorizationRequest(
        // Maybe how email so and login
        String input,
        String password
) {
}
