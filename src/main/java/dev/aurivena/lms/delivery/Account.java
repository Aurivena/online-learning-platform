package dev.aurivena.lms.delivery;

import dev.aurivena.lms.domain.models.Account.AccountAuthorizationResponse;
import dev.aurivena.lms.domain.models.Account.AccountCreateResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Account {
    public  Account() {
    }

    @PostMapping("/registration")
    public void createAccount(@Valid @RequestBody AccountCreateResponse account) {
        return;
    }

    @PostMapping("/authorization")
    public void authorizeAccount(@Valid @RequestBody AccountAuthorizationResponse account) {
            return;
    }
}
