package dev.aurivena.lms.domain.course;

import dev.aurivena.lms.common.api.Spond;
import dev.aurivena.lms.domain.course.dto.CourseResponse;
import dev.aurivena.lms.domain.course.dto.CreateCourseRequest;

import java.util.List;

import dev.aurivena.lms.domain.course.dto.UpdateCourseRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/organizations/{organizationId}/courses")
@Tag(name = "Course", description = "Работа с курсами организации")
@RequiredArgsConstructor
class CourseController {
    private final CourseService courseService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(produces = "application/json")
    public Spond<CourseResponse> create(@RequestBody CreateCourseRequest request, @PathVariable Long organizationId, @AuthenticationPrincipal String ownerEmail) {
        return Spond.success(courseService.create(request, organizationId, ownerEmail));
    }

    @GetMapping(value = "/{courseId}", produces = "application/json")
    public Spond<CourseResponse> getByID(@PathVariable long courseId, @PathVariable Long organizationId) {
        return Spond.success(courseService.findById(courseId, organizationId));
    }

    @GetMapping(produces = "application/json")
    public Spond<List<CourseResponse>> getAllByOrganization(@PathVariable Long organizationId) {
        return Spond.success(courseService.findAllByOrganizationId(organizationId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{courseId}")
    public Spond<CourseResponse> update(@RequestBody UpdateCourseRequest request, @PathVariable Long courseId, @PathVariable Long organizationId) {
        return Spond.success(courseService.update(request, courseId, organizationId));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{courseId}")
    public Spond<Void> delete(@PathVariable Long courseId, @PathVariable Long organizationId, @AuthenticationPrincipal String ownerEmail) {
        courseService.delete(courseId, organizationId, ownerEmail);
        return Spond.success(null);
    }
}
