package dev.aurivena.lms.domain.module;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


interface ModuleRepository  extends JpaRepository<Module, Long> {
    Optional<Module> findById(Long id);
}
