package dev.aurivena.lms.domain.account.dto;

public record AccountResponse(
        String email,
        String login,
        String username,
        String avatar
) {
}
