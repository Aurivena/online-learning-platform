package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.common.security.JwtService;
import dev.aurivena.lms.domain.account.dto.AccountResponse;
import dev.aurivena.lms.domain.account.dto.AuthRequest;
import dev.aurivena.lms.domain.account.dto.RegistrationRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static dev.aurivena.lms.domain.account.JwtType.ACCESS;
import static dev.aurivena.lms.domain.account.JwtType.REFRESH;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AccountMapper accountMapper;
    private final JwtService jwtService;
    private final RefreshRepository refreshRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public TokenPair login(AuthRequest request) {
        var account = accountRepository.findByEmailOrLogin(request.input(), request.input())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.password(), account.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }

        String accessToken = jwtService.generate(ACCESS, account.getEmail(), account.getRole().name());
        String refreshTokenString = jwtService.generate(REFRESH, account.getEmail(), account.getRole().name());

        RefreshToken refreshTokenEntity = RefreshToken.builder()
                .account(account)
                .tokenHash(passwordEncoder.encode(refreshTokenString))
                .expiration(Instant.now().plusMillis(REFRESH.getDuration()))
                .build();

        refreshRepository.save(refreshTokenEntity);

        return new TokenPair(accessToken, refreshTokenString);
    }

    @Transactional
    public void register(RegistrationRequest request) {
        if (accountRepository.existsByEmail(request.email())) {
            throw new RuntimeException("Email already exists");
        }
        if (accountRepository.existsByLogin(request.login())) {
            throw new RuntimeException("Login already exists");
        }

        Account account = accountMapper.toEntity(request);

        account.setPasswordHash(passwordEncoder.encode(request.password()));

        accountRepository.save(account);
    }

    @Transactional
    public AccountResponse getAccountByEmail(String email) {
        if  (accountRepository.existsByEmail(email)) {
            return accountMapper.toResponse(accountRepository.findByEmailIgnoreCase(email).get());
        }
        return null;
    }
}
