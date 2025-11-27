package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.domain.account.dto.AuthRequest;
import dev.aurivena.lms.domain.account.dto.AuthResponse;
import dev.aurivena.lms.common.api.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static dev.aurivena.lms.domain.account.JwtType.REFRESH;

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
    public ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        TokenPair tokens =  authService.login(request);

        AuthResponse body = new AuthResponse(tokens.accessToken());

        Cookie cookie = new Cookie("refresh_token", tokens.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) REFRESH.getDuration() / 1000);

        response.addCookie(cookie);

        return ApiResponse.success(body);
    }
}
