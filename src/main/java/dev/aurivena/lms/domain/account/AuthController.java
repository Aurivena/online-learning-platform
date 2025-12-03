package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.common.api.Spond;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.aurivena.lms.domain.account.JwtType.REFRESH;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Авторизация и регистрация")
@RequiredArgsConstructor
class AuthController {
    private final AccountService authService;

    @Operation(
            summary = "Вход в систему",
            description = "Возвращает Access Token в теле и устанавливает Refresh Token в HttpOnly Cookie"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или неверные данные",
                    content = @Content(schema = @Schema(implementation = Spond.class)))
    })
    @PostMapping(value = "/login", produces = "application/json")
    @SecurityRequirements()
    public Spond<AuthResponse> login(@Valid @RequestBody AuthRequest request, HttpServletResponse response) {
        try {
            TokenPair tokens = authService.login(request);

            AuthResponse body = new AuthResponse(tokens.accessToken());

            setCookie(response, tokens.refreshToken());

            return Spond.success(body);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Operation(
            summary = "Регистрация в системе",
            description = "Возвращает Access Token в теле и устанавливает Refresh Token в HttpOnly Cookie"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Аккаунт успешно создан"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или неверные данные",
                    content = @Content(schema = @Schema(implementation = Spond.class)))
    })
    @PostMapping(value = "/register", produces = "application/json")
    @SecurityRequirements()
    public Spond<AuthResponse> registerAccount(@Valid @RequestBody RegistrationRequest request, HttpServletResponse response) {
        try {
            authService.register(request);
            AuthRequest authRequest = new AuthRequest(request.email(), request.password());
            TokenPair tokens = authService.login(authRequest);

            AuthResponse body = new AuthResponse(tokens.accessToken());

            setCookie(response, tokens.refreshToken());

            return Spond.success(body);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Operation(
            summary = "Выход из системы",
            description = "Удаляет refresh token"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный выход",
                    content = @Content(schema = @Schema(implementation = Spond.class)))
    })
    @PostMapping(value = "/logout", produces = "application/json")
    public Spond<String> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("refresh_token", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        //TODO реализовать, чтобы удалялось из бд

        return Spond.success("Logged out");
    }

    private void setCookie(HttpServletResponse response, String refreshToken) {
        Cookie cookie = new Cookie("refresh_token", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth/refresh");
        cookie.setMaxAge((int) REFRESH.getDuration() / 1000);

        response.addCookie(cookie);
    }
}
