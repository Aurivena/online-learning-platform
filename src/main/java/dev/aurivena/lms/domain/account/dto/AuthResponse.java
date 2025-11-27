package dev.aurivena.lms.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Ответ при успешном входе")
public record AuthResponse(
        @Schema(description = "JWT токен доступа (живет 15 минут)", example = "eyJhbGciOiJIUzI1Ni...")
        String accessToken
) {
}
