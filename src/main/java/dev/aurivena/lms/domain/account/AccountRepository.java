package dev.aurivena.lms.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByEmailOrLogin(String email, String login);

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);
}
