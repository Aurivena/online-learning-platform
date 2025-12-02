package dev.aurivena.lms.domain.course;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findById(Long id);

    List<Course> findAllByOrganizationId(Long organizationId);
}
