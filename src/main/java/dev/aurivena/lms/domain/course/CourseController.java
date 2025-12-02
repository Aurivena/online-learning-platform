package dev.aurivena.lms.domain.course;

import dev.aurivena.lms.domain.course.dto.CourseResponse;
import dev.aurivena.lms.domain.course.dto.CreateCourseRequest;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations/{organizationId}/courses")
@Tag(name = "Course", description = "Работа с курсами организации")
@RequiredArgsConstructor
class CourseController {
    private final CourseService courseService;

    @PostMapping(produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<CourseResponse> create(@RequestBody CreateCourseRequest request, @PathVariable Long organizationId, @AuthenticationPrincipal String ownerEmail) {
        return dev.aurivena.lms.common.api.ApiResponse.success(courseService.create(request, organizationId, ownerEmail));
    }

    @GetMapping(value = "/{courseId}", produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<CourseResponse> getByID(@PathVariable long courseId, @PathVariable Long organizationId) {
        return dev.aurivena.lms.common.api.ApiResponse.success(courseService.findById(courseId, organizationId));
    }

    @GetMapping(produces = "application/json")
    public dev.aurivena.lms.common.api.ApiResponse<List<CourseResponse>> getAllByOrganization(@PathVariable Long organizationId) {
        return dev.aurivena.lms.common.api.ApiResponse.success(courseService.findAllByOrganizationId(organizationId));
    }
}
