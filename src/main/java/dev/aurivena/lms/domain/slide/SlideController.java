package dev.aurivena.lms.domain.slide;

import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/modules/{moduleId}/slides")
@Tag(name = "Slide", description = "Работа со слайдами модуля")
@RequiredArgsConstructor
class SlideController {
    private final SlideService slideService;


    @PostMapping(produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<SlideResponse> create(@RequestBody CreateSlideRequest request, @PathVariable long moduleId) {
        return dev.aurivena.lms.common.api.ApiResponse.success(slideService.create(request, moduleId));
    }

    @GetMapping(value = "/{slideId}", produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<SlideResponse> getByID(@PathVariable long slideId, @PathVariable long moduleId) {
        return dev.aurivena.lms.common.api.ApiResponse.success(slideService.findById(slideId, moduleId));
    }
}
