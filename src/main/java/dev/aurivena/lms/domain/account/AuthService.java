package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.domain.account.dto.AccountResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountMapper accountMapper;
    public AccountResponse login(@Valid @RequestBody Account account) {
        return accountMapper.toResponse(account);
    }

    public void register(@Valid @RequestBody Account account) {
    }
}
