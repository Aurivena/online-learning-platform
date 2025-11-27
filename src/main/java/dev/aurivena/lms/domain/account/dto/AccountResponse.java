package dev.aurivena.lms.domain.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import dev.aurivena.lms.domain.account.Role;

import java.time.Instant;

@Schema(description = "Модель для получения основной информации о пользователе")
public record AccountResponse(
        @Schema(description = "ID пользователя", example = "1")
        Long id,

        @Schema(description = "Email", example = "user@mail.ru")
        String email,

        @Schema(description = "Логин (никнейм)", example = "nagibator2000")
        String login,

        @Schema(description = "Отображаемое имя", example = "Василий Пупкин")
        String username,

        @Schema(description = "Роль в системе", example = "USER")
        Role role,

        @Schema(description = "Дата регистрации (UTC)", example = "2023-11-27T10:00:00Z")
        Instant createdAt
) {}