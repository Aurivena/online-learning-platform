package dev.aurivena.lms.domain.slide.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import dev.aurivena.lms.domain.slide.SlideType;


public record CreateSlideRequest(
        String title,
        String description,

        @JsonProperty("slide_type")
        SlideType slideType,
        JsonNode payload
) {
}