package dev.aurivena.lms.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshRepository extends JpaRepository<RefreshToken, Long> {
}
 