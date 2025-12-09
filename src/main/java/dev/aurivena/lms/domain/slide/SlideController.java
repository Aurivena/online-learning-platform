package dev.aurivena.lms.domain.slide;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aurivena.lms.common.api.Spond;
import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import dev.aurivena.lms.domain.slide.dto.UpdateSlideRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/modules/{moduleId}/slides")
@Tag(name = "Slide", description = "Работа со слайдами модуля")
@RequiredArgsConstructor
class SlideController {
    private final SlideService slideService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public SlideResponse create(
            @PathVariable Long moduleId,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam("slideType") SlideType slideType,
            @RequestParam(value = "payload", required = false) String payloadRaw,
            @RequestParam(value = "file", required = false) MultipartFile file
    ) throws Exception {

        JsonNode payload = null;
        if (payloadRaw != null && !payloadRaw.isBlank()) {
            payload = objectMapper.readTree(payloadRaw);
        }

        CreateSlideRequest request = new CreateSlideRequest(
                title,
                description,
                slideType,
                payload
        );

        return slideService.create(request, moduleId, file);
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
