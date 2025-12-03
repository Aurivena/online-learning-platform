package dev.aurivena.lms.domain.module.dto;

import dev.aurivena.lms.domain.slide.dto.SlideResponse;

import java.util.List;

public record ModuleResponse(
        Long id,
        String title,
        List<SlideResponse> slides
) {
}
