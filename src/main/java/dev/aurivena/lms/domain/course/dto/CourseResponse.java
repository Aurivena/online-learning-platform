package dev.aurivena.lms.domain.course.dto;

import dev.aurivena.lms.domain.module.dto.ModuleResponse;

import java.math.BigDecimal;
import java.util.List;

public record CourseResponse(
        Long id,
        String title,
        String description,
        BigDecimal price,
        List<ModuleResponse> modules
) {
}
