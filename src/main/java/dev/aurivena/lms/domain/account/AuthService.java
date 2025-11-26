package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.common.security.JwtService;
import dev.aurivena.lms.domain.account.dto.AuthResponse;
import dev.aurivena.lms.domain.account.dto.AuthRequest;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

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
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
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
                .expiration(Instant.now().plusMillis(REFRESH.getValue()))
                .build();

        refreshRepository.save(refreshTokenEntity);

        return new AuthResponse(accessToken, refreshTokenString);
    }

    public void register(@Valid @RequestBody Account account) {
    }
}
