package dev.aurivena.lms.domain.slide;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.aurivena.lms.common.api.Spond;
import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import dev.aurivena.lms.domain.slide.dto.UpdateSlideRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/modules/{moduleId}/slides")
@Tag(name = "Slide", description = "Работа со слайдами модуля")
@RequiredArgsConstructor
class SlideController {
    private final SlideService slideService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public SlideResponse create(
            @PathVariable Long moduleId,
            @RequestPart("request") CreateSlideRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file
    ) throws Exception {
        return slideService.create(request, moduleId, file);
    }

    @GetMapping(value = "/{slideId}", produces = "application/json")
    public Spond<SlideResponse> getByID(@PathVariable long slideId, @PathVariable long moduleId) {
        return Spond.success(slideService.findById(slideId, moduleId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{slideId}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Spond<SlideResponse> update(
            @RequestPart("request") UpdateSlideRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file,
            @PathVariable long slideId,
            @PathVariable long moduleId) throws IOException {
        return Spond.success(slideService.update(request, file, slideId, moduleId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{slideId}")
    public Spond<Void> delete(@PathVariable long slideId, @PathVariable long moduleId, @AuthenticationPrincipal String ownerEmail) {
        slideService.delete(slideId, moduleId, ownerEmail);
        return Spond.success(null);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/reorder")
    public Spond<Void> recorder(@PathVariable long moduleId, @RequestBody List<Long> newOrderIds) {
        slideService.reorder(moduleId, newOrderIds);
        return Spond.success(null);
    }
}
