package dev.aurivena.lms.domain.slide;

import dev.aurivena.lms.common.api.Spond;
import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import dev.aurivena.lms.domain.slide.dto.UpdateSlideRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules/{moduleId}/slides")
@Tag(name = "Slide", description = "Работа со слайдами модуля")
@RequiredArgsConstructor
class SlideController {
    private final SlideService slideService;


    @PostMapping(produces = "application/json")
    public Spond<SlideResponse> create(@RequestBody CreateSlideRequest request, @PathVariable long moduleId) {
        return Spond.success(slideService.create(request, moduleId));
    }

    @GetMapping(value = "/{slideId}", produces = "application/json")
    public Spond<SlideResponse> getByID(@PathVariable long slideId, @PathVariable long moduleId) {
        return Spond.success(slideService.findById(slideId, moduleId));
    }

    @PutMapping("/{slideId}")
    public Spond<SlideResponse> update(@RequestBody UpdateSlideRequest request, @PathVariable long slideId, @PathVariable long moduleId) {
        return Spond.success(slideService.update(request, slideId, moduleId));
    }

    @DeleteMapping("/{slideId}")
    public Spond<Void> delete(@PathVariable long slideId, @PathVariable long moduleId, @AuthenticationPrincipal String ownerEmail) {
        slideService.delete(slideId, moduleId, ownerEmail);
        return Spond.success(null);
    }

    @PutMapping("/reorder")
    public Spond<Void> recorder(@PathVariable long moduleId, @RequestBody List<Long> newOrderIds) {
        slideService.reorder(moduleId, newOrderIds);
        return Spond.success(null);
    }
}
