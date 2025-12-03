package dev.aurivena.lms.domain.course.dto;

import java.math.BigDecimal;

public record UpdateCourseRequest(
        String title,
        BigDecimal price
) {
}
