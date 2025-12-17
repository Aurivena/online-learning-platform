package dev.aurivena.lms.domain.account;

import dev.aurivena.lms.common.security.JwtService;
import dev.aurivena.lms.domain.account.dto.AuthRequest;
import dev.aurivena.lms.domain.account.dto.RegistrationRequest;
import dev.aurivena.lms.domain.organization.Organization;
import dev.aurivena.lms.domain.organization.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountMapper accountMapper;
    @Mock
    private JwtService jwtService;
    @Mock
    private RefreshRepository refreshRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void login_Success() {
        // Arrange
        String input = "test@example.com";
        String password = "password";
        AuthRequest request = new AuthRequest(input, password);
        Account account = Account.builder()
                .email(input)
                .passwordHash("encodedPassword")
                .role(Role.USER)
                .build();

        when(accountRepository.findByEmailOrLogin(input, input)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(true);
        when(jwtService.generate(any(), any(), any())).thenReturn("token");

        // Act
        TokenPair result = accountService.login(request);

        // Assert
        assertNotNull(result);
        assertEquals("token", result.accessToken());
        assertEquals("token", result.refreshToken());
        verify(refreshRepository).save(any(RefreshToken.class));
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        String input = "test@example.com";
        String password = "password";
        AuthRequest request = new AuthRequest(input, password);

        when(accountRepository.findByEmailOrLogin(input, input)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> accountService.login(request));
    }

    @Test
    void login_InvalidPassword() {
        // Arrange
        String input = "test@example.com";
        String password = "password";
        AuthRequest request = new AuthRequest(input, password);
        Account account = Account.builder()
                .email(input)
                .passwordHash("encodedPassword")
                .role(Role.USER)
                .build();

        when(accountRepository.findByEmailOrLogin(input, input)).thenReturn(Optional.of(account));
        when(passwordEncoder.matches(password, "encodedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> accountService.login(request));
    }

    @Test
    void register_Success() {
        // Arrange
        Organization organization = new Organization();
        organization.setId(1L);
        organization.setMembers(new ArrayList<>());
        RegistrationRequest request = new RegistrationRequest("test@example.com", "User Name", "login", "password", organization);

        when(accountRepository.existsByEmail(request.email())).thenReturn(false);
        when(accountRepository.existsByLogin(request.login())).thenReturn(false);
        when(organizationRepository.findById(request.organization().getId())).thenReturn(Optional.of(organization));

        Account account = new Account();
        when(accountMapper.toEntity(request)).thenReturn(account);
        when(passwordEncoder.encode(request.password())).thenReturn("encodedPassword");

        // Act
        accountService.register(request);

        // Assert
        verify(accountRepository).save(account);
        verify(organizationRepository).save(organization);
        assertEquals("encodedPassword", account.getPasswordHash());
        assertTrue(organization.getMembers().contains(account));
    }

    @Test
    void register_EmailExists() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("test@example.com", "User Name", "login", "password", new Organization());
        when(accountRepository.existsByEmail(request.email())).thenReturn(true);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> accountService.register(request));
    }

    @Test
    void register_LoginExists() {
        // Arrange
        RegistrationRequest request = new RegistrationRequest("test@example.com", "User Name", "login", "password", new Organization());
        when(accountRepository.existsByEmail(request.email())).thenReturn(false);
        when(accountRepository.existsByLogin(request.login())).thenReturn(true);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> accountService.register(request));
    }

    @Test
    void register_OrganizationNotFound() {
        // Arrange
        Organization organization = new Organization();
        organization.setId(1L);
        RegistrationRequest request = new RegistrationRequest("test@example.com", "User Name", "login", "password", organization);

        when(accountRepository.existsByEmail(request.email())).thenReturn(false);
        when(accountRepository.existsByLogin(request.login())).thenReturn(false);
        when(organizationRepository.findById(organization.getId())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> accountService.register(request));
    }

    @Test
    void getAccountByEmail_Found() {
        // Arrange
        String email = "test@example.com";
        Account account = new Account();
        when(accountRepository.existsByEmail(email)).thenReturn(true);
        when(accountRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(account));

        // Act
        Account result = accountService.getAccountByEmail(email);

        // Assert
        assertNotNull(result);
        assertEquals(account, result);
    }

    @Test
    void getAccountByEmail_NotFound() {
        // Arrange
        String email = "test@example.com";
        when(accountRepository.existsByEmail(email)).thenReturn(false);

        // Act
        Account result = accountService.getAccountByEmail(email);

        // Assert
        assertNull(result);
    }
}
