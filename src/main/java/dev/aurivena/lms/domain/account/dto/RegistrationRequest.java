package dev.aurivena.lms.domain.account.dto;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import dev.aurivena.lms.domain.organization.Organization;

public record RegistrationRequest(
        @NotBlank @Email
        String email,

        @NotBlank @Size(min = 3, max = 60)
        String username,

        @NotBlank @Size(min = 3, max = 30)
        String login,

        @NotBlank @Size(min = 6, max = 100)
        String password,

        @NotNull
        Organization organization
) {
}
