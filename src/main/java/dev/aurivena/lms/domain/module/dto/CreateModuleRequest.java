package dev.aurivena.lms.domain.module.dto;

import dev.aurivena.lms.domain.slide.Slide;

import java.util.List;

public record CreateModuleRequest(
        String title,
        List<Slide> slides
) {
}
