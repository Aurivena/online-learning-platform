package dev.aurivena.lms.domain.course.dto;

import dev.aurivena.lms.domain.account.Account;

import java.text.DecimalFormat;
import java.util.List;

public record CourseResponse(
        String title,
        String description,
        DecimalFormat price,
        List<Module> modules,
        Account owner
) {
}
