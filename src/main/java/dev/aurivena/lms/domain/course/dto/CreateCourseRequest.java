package dev.aurivena.lms.domain.course.dto;

import java.text.DecimalFormat;
import java.util.List;

public record CreateCourseRequest(
        String title,
        String description,
        DecimalFormat price,
        List<Module> modules
) {
}
