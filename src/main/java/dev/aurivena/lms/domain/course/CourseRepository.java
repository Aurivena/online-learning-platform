package dev.aurivena.lms.domain.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findById(Long id);

    List<Course> findAllByOrganizationId(Long organizationId);

    @Query("""
            SELECT c FROM Course c
            JOIN c.modules cm 
            WHERE cm.module.id = :moduleId
            """)
    Optional<Course> findByModuleId(@Param("moduleId") Long moduleId);
}
