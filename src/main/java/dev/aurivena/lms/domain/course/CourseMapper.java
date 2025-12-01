package dev.aurivena.lms.domain.course;

import dev.aurivena.lms.domain.course.dto.CourseResponse;
import dev.aurivena.lms.domain.course.dto.CreateCourseRequest;
import dev.aurivena.lms.domain.module.ModuleMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ModuleMapper.class})
interface CourseMapper {
    CourseResponse toResponse(Course course);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    Course toEntity(CreateCourseRequest request);
}
