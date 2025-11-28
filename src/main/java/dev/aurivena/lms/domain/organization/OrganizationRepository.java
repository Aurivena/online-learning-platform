package dev.aurivena.lms.domain.organization;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.domain.Pageable;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    @Query("""
                SELECT o FROM Organization o 
                JOIN o.owner u 
                WHERE (:login IS NULL OR u.email = :login OR u.login = :login) 
                AND (:tag IS NULL OR o.tag = :tag) 
            """)
    Page<Organization> search(String login, String tag, Pageable pageable);
}
