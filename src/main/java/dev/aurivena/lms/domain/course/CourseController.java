package dev.aurivena.lms.domain.course;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/organizations/{id}/courses")
@Tag(name = "Course", description = "Работа с курсами организации")
@RequiredArgsConstructor
class CourseController {

}
