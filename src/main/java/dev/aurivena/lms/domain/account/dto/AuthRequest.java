package dev.aurivena.lms.domain.account.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
        // Maybe how email so and login
        @NotBlank(message = "Email or Login cannot be empty")
        String input,

        @NotBlank
        String password
) {
}
