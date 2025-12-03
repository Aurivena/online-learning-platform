package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.common.api.Spond;
import dev.aurivena.lms.domain.module.dto.CreateModuleRequest;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import dev.aurivena.lms.domain.module.dto.UpdateModuleRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses/{courseId}/modules")
@Tag(name = "Module", description = "Работа с модулями курса")
@RequiredArgsConstructor
class ModuleController {
    private final ModuleService moduleService;


    @PostMapping(produces = "application/json")
    public Spond<ModuleResponse> create(@RequestBody CreateModuleRequest request, @PathVariable Long courseId) {
        return Spond.success(moduleService.create(request, courseId));
    }

    @GetMapping(value = "/{moduleId}", produces = "application/json")
    public Spond<ModuleResponse> getByID(@PathVariable long moduleId, @PathVariable Long courseId) {
        return Spond.success(moduleService.findById(moduleId, courseId));
    }

    @PutMapping("/{moduleId}")
    public Spond<ModuleResponse> update(@RequestBody UpdateModuleRequest request, @PathVariable long moduleId, @PathVariable Long courseId) {
        return Spond.success(moduleService.update(request,moduleId,courseId));
    }

    @DeleteMapping("/{moduleId}")
    public Spond<Void> delete(@PathVariable long moduleId, @PathVariable Long courseId,@AuthenticationPrincipal String ownerEmail) {
        moduleService.delete(moduleId, courseId, ownerEmail);
        return Spond.success(null);
    }
}
