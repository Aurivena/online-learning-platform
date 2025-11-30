package dev.aurivena.lms.domain.module;

import org.springframework.data.jpa.repository.JpaRepository;

interface ModuleRepository  extends JpaRepository<Module, Long> {
}
