package dev.aurivena.lms.domain.account.dto;

public record UpdateProfileRequest(
        long id,
        String username,
        String description
) {
}
