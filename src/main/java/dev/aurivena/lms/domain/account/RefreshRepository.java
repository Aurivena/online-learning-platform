package dev.aurivena.lms.domain.account;

import org.springframework.data.jpa.repository.JpaRepository;

interface RefreshRepository extends JpaRepository<RefreshToken, Long> {
}
 