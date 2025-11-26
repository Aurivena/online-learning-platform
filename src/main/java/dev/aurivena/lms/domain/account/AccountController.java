package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.domain.account.dto.AuthRequest;
import dev.aurivena.lms.domain.account.dto.AuthResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AccountController {

    private final AuthService authService;

    public AccountController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @SecurityRequirements()
    public void registerAccount(final Account account) {
        authService.register(account);
    }

    @PostMapping("/login")
    @SecurityRequirements()
    public AuthResponse login(final AuthRequest request) {
        return authService.login(request);
    }
}
