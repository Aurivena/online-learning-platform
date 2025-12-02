package dev.aurivena.lms.domain.organization;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query("""
        SELECT o FROM Organization o 
        JOIN o.owner u 
        WHERE (:login IS NULL OR u.email = :login OR u.login = :login) 
        AND (:tag IS NULL OR o.tag = :tag) 
    """)
    Page<Organization> search(
            @Param("login") String login,
            @Param("tag") String tag,
            Pageable pageable
    );

    Optional<Organization> findByTag(String tag);
    Optional<Organization> findById(Long id);
}
