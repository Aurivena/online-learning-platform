package dev.aurivena.lms.domain.module;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/courses/{id}/modules")
@Tag(name = "Module", description = "Работа с модулями курса")
@RequiredArgsConstructor
class ModuleController {
}
