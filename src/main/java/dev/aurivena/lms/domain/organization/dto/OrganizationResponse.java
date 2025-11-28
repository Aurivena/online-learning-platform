package dev.aurivena.lms.domain.organization.dto;

import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.account.dto.AccountResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.List;

@Schema(description = "Модель для получения основной информации о организации")
public record OrganizationResponse(
        @Schema(description = "ID организации", example = "1")
        Long id,

        @Schema(description = "Навзание организации", example = "Skillbox 2.0")
        String title,

        @Schema(description = "Отображаемое описание", example = "Мы крутые!")
        String description,

        @Schema(description = "Уникальный тэг организации", example = "GRPC")
        String tag,

        @Schema(description = "Создатель организации", example = "nagibator 2000")
        AccountResponse owner,

        @Schema(description = "Список участников")
        List<AccountResponse> members,

        @Schema(description = "Дата создания (UTC)", example = "2023-11-27T10:00:00Z")
        Instant createdAt
) {

}
