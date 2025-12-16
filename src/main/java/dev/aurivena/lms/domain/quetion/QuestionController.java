package dev.aurivena.lms.domain.quetion;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/modules/{moduleId}/question")
@Tag(name = "Course", description = "Работа с тестами организации")
@RequiredArgsConstructor
public class QuestionController {

}
