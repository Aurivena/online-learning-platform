package dev.aurivena.lms.domain.slide.dto;

import com.fasterxml.jackson.databind.JsonNode;
import dev.aurivena.lms.domain.slide.SlideType;

public record CreateSlideRequest(
        String title,
        String description,
        SlideType slideType,
        JsonNode payload
) {
}
