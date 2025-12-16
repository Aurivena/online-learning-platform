package dev.aurivena.lms.domain.slide.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import dev.aurivena.lms.domain.slide.Payload;
import dev.aurivena.lms.domain.slide.SlideType;

public record UpdateSlideRequest(
        String title,
        String description,

        @JsonProperty("slide_type")
        SlideType slideType,
        Payload payload
) {
}