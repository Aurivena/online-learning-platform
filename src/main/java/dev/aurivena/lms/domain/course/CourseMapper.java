package dev.aurivena.lms.domain.course;

import dev.aurivena.lms.domain.course.dto.CourseResponse;
import dev.aurivena.lms.domain.course.dto.CreateCourseRequest;
import dev.aurivena.lms.domain.module.ModuleMapper;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ModuleMapper.class})
public abstract class CourseMapper {

    @Autowired
    protected ModuleMapper moduleMapper;

    @Mapping(target = "modules", expression = "java(mapModules(course.getModules()))")
    public abstract CourseResponse toResponse(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "organization", ignore = true)
    @Mapping(target = "modules", ignore = true)
    public abstract Course toEntity(CreateCourseRequest request);

    protected List<ModuleResponse> mapModules(List<CourseModule> courseModules) {
        if (courseModules == null) {
            return List.of();
        }

        return courseModules.stream()
                .map(cm -> moduleMapper.toResponse(cm.getModule()))
                .toList();
    }
}