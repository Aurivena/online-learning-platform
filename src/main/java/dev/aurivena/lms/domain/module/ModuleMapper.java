package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.domain.module.dto.CreateModuleRequest;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import dev.aurivena.lms.domain.slide.SlideMapper;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {SlideMapper.class})
public abstract class ModuleMapper {
    @Autowired
    private SlideMapper slideMapper;

    @Mapping(target = "slides", expression = "java(mapSlides(module.getSlides()))")
    public abstract ModuleResponse toResponse(Module module);

    protected List<SlideResponse> mapSlides(List<ModuleSlide> moduleSlides) {
        return moduleSlides.stream()
                .map(ms -> slideMapper.toResponse(ms.getSlide()))
                .toList();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "slides", ignore = true)
    public abstract Module toEntity(CreateModuleRequest request);
}
