package dev.aurivena.lms.domain.slide;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/modules/{id}/slides")
@Tag(name = "Slide", description = "Работа со слайдами модуля")
@RequiredArgsConstructor
class SlideController {
    private SlideService slideService;


}
