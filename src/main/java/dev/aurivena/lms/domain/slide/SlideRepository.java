package dev.aurivena.lms.domain.slide;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


interface SlideRepository extends JpaRepository<Slide, Long> {
    Optional<Slide> findById(Long id);
}
