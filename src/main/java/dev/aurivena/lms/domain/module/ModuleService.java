package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.domain.course.Course;
import dev.aurivena.lms.domain.course.CourseModule;
import dev.aurivena.lms.domain.course.CourseRepository;
import dev.aurivena.lms.domain.module.dto.CreateModuleRequest;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import dev.aurivena.lms.domain.module.dto.UpdateModuleRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class ModuleService {
    private final ModuleRepository moduleRepository;
    private final ModuleMapper moduleMapper;
    private final CourseRepository courseRepository;

    @Transactional
    public ModuleResponse create(CreateModuleRequest request, Long courseId) {
        Module module = moduleRepository.save(moduleMapper.toEntity(request));
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("course not found"));
        CourseModule link = CourseModule.create(course, module, course.getModules().size() + 1);

        course.getModules().add(link);

        courseRepository.save(course);

        return moduleMapper.toResponse(module);
    }


    @Transactional
    ModuleResponse findById(Long moduleId, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseModule link = course.getModules().stream()
                .filter(cm -> cm.getModule().getId().equals(moduleId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Module not found in this course"));

        return moduleMapper.toResponse(link.getModule());
    }

    @Transactional
    public ModuleResponse update(UpdateModuleRequest request, long moduleId, Long courseId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("module not found"));

        module.setTitle(request.title());

        return moduleMapper.toResponse(module);
    }
}
