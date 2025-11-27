package dev.aurivena.lms.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос на вход в систему")
public record AuthRequest(
        @Schema(description = "Email или Логин пользователя", example = "nagibator2000")
        @NotBlank(message = "Email or Login cannot be empty")
        String input,

        @Schema(description = "Пароль", example = "secret_password_123")
        @NotBlank
        String password
) {
}
