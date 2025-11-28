package dev.aurivena.lms.domain.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Создание организации")
public record CreateOrganizationRequest(
        @NotBlank @Size(min = 3, max = 100)
        String title,

        @NotBlank
        @Size(min = 2, max = 20)
        @Pattern(regexp = "^[A-Za-z0-9_]+$", message = "Тег должен содержать только буквы, цифры и подчеркивания")
        String tag,

        String description
) {}