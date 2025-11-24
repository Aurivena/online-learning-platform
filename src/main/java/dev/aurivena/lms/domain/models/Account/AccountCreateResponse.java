package dev.aurivena.lms.domain.models.Account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountCreateResponse(
        @NotBlank @Size(min = 2, max = 125) String username,
        @NotBlank @Size (min = 3, max = 125) String login,
        @NotBlank @Size(min = 8, max = 66) String password,
        @NotBlank @Email String email
) {
}
