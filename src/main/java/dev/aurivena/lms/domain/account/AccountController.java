package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.domain.account.dto.AuthRequest;
import dev.aurivena.lms.domain.account.dto.AuthResponse;
import dev.aurivena.lms.domain.account.dto.RegistrationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.aurivena.lms.domain.account.JwtType.REFRESH;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Авторизация и регистрация")
@RequiredArgsConstructor
public class AccountController {
    private final AuthService authService;

    @PostMapping("/register")
    @SecurityRequirements()
    public void registerAccount(@Valid @RequestBody RegistrationRequest request) {
        authService.register(request);
    }

    @Operation(
            summary = "Вход в систему",
            description = "Возвращает Access Token в теле и устанавливает Refresh Token в HttpOnly Cookie"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или неверные данные",
                    content = @Content(schema = @Schema(implementation = dev.aurivena.lms.common.api.ApiResponse.class)))
    })
    @PostMapping("/login")
    @SecurityRequirements()
    public dev.aurivena.lms.common.api.ApiResponse<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        TokenPair tokens = authService.login(request);

        AuthResponse body = new AuthResponse(tokens.accessToken());

        Cookie cookie = new Cookie("refresh_token", tokens.refreshToken());
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) REFRESH.getDuration() / 1000);

        response.addCookie(cookie);

        return dev.aurivena.lms.common.api.ApiResponse.success(body);
    }
}
