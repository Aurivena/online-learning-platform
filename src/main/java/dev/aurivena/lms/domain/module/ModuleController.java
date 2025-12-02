package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.domain.module.dto.CreateModuleRequest;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses/{courseId}/modules")
@Tag(name = "Module", description = "Работа с модулями курса")
@RequiredArgsConstructor
class ModuleController {
    private final ModuleService moduleService;


    @PostMapping(produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<ModuleResponse> create(@RequestBody CreateModuleRequest request, @PathVariable Long courseId) {
        return dev.aurivena.lms.common.api.ApiResponse.success(moduleService.create(request, courseId));
    }

    @GetMapping(value = "/{moduleId}", produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<ModuleResponse> getByID(@PathVariable long moduleId, @PathVariable Long courseId) {
        return dev.aurivena.lms.common.api.ApiResponse.success(moduleService.findById(moduleId, courseId));
    }
}
