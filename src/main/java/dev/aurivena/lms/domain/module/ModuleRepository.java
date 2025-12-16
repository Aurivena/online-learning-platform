package dev.aurivena.lms.domain.module;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;


public interface ModuleRepository extends JpaRepository<Module, Long> {
    @Query("""
    select distinct m
    from Module m
    left join fetch m.slides ms
    left join fetch ms.slide s
    where m.id = :id
  """)
    Optional<Module> findByIdWithSlides(@Param("id") Long id);
}
