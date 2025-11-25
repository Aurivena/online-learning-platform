package dev.aurivena.lms.domain.account.dto;


public record RegistrationRequest(
        String email,
        String username,
        String login,
        String password
) {
}
