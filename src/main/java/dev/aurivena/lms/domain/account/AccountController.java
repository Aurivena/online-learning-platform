package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.domain.account.dto.AccountResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Account", description = "Работа с пользователем")
@RequiredArgsConstructor
public class AccountController {
    private final AccountService authService;
    private final AccountMapper accountMapper;

    @Operation(
            summary = "Данные о пользователе",
            description = "Возвращает данные о пользователе по его email"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Получение данных"),
            @ApiResponse(responseCode = "404", description = "Такого пользователя не существует",
                    content = @Content(schema = @Schema(implementation = dev.aurivena.lms.common.api.ApiResponse.class)))
    })
    @GetMapping(value = "/me", produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<AccountResponse> getAccount(@AuthenticationPrincipal String email) {
        AccountResponse body = accountMapper.toResponse(authService.getAccountByEmail(email));

        if (body == null) {
            return dev.aurivena.lms.common.api.ApiResponse.fail("404");
        }

        return dev.aurivena.lms.common.api.ApiResponse.success(body);
    }
}
