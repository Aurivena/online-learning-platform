package dev.aurivena.lms.domain.slide;

import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface SlideMapper {
    SlideResponse toResponse(Slide slide);

    @Mapping(target = "id", ignore = true)
    Slide toEntity(CreateSlideRequest request);
}
