package dev.aurivena.lms.domain.course.dto;

import dev.aurivena.lms.domain.module.ModuleSlide;

import java.math.BigDecimal;
import java.util.List;

public record CreateCourseRequest(
        String title,
        String description,
        BigDecimal price,
        List<ModuleSlide> modules
) {
}
