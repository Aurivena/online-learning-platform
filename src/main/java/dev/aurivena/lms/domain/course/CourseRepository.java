package dev.aurivena.lms.domain.course;

import org.springframework.data.jpa.repository.JpaRepository;

interface CourseRepository extends JpaRepository<Course, Long> {
}
