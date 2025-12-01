package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.domain.module.dto.CreateModuleRequest;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import dev.aurivena.lms.domain.slide.SlideMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {SlideMapper.class})
public interface ModuleMapper {
    ModuleResponse toResponse(Module module);

    @Mapping(target = "id", ignore = true)
    Module toEntity(CreateModuleRequest request);
}
