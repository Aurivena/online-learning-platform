package dev.aurivena.lms.domain.quetion.dto;

import dev.aurivena.lms.domain.quetion.Option;

import java.util.List;

public record QuestionResponse(
        Long id,
        String text,
        List<Option> options
) {

}
