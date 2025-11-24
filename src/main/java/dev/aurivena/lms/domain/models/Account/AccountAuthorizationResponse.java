package dev.aurivena.lms.domain.models.Account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record AccountAuthorizationResponse(
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 66) String password
) {
}
