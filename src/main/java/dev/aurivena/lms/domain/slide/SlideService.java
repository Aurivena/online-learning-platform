package dev.aurivena.lms.domain.slide;

import dev.aurivena.lms.domain.module.Module;
import dev.aurivena.lms.domain.module.ModuleRepository;
import dev.aurivena.lms.domain.module.ModuleSlide;
import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import dev.aurivena.lms.domain.slide.dto.UpdateSlideRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
class SlideService {
    private final SlideMapper slideMapper;
    private final SlideRepository slideRepository;
    private final ModuleRepository moduleRepository;

    @Transactional
    public SlideResponse create(CreateSlideRequest request, Long moduleId) {
        Slide slide = slideRepository.save(slideMapper.toEntity(request));
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("module not found"));
        ModuleSlide link = ModuleSlide.create(module, slide, module.getSlides().size() + 1);

        module.getSlides().add(link);
        moduleRepository.save(module);

        return slideMapper.toResponse(slide);
    }

    @Transactional
    public SlideResponse findById(Long slideId, long moduleId) {
        return slideMapper.toResponse(getSlideBySlideIdAndModuleId(slideId, moduleId).getSlide());
    }

    @Transactional
    public SlideResponse update(UpdateSlideRequest request, long slideId, long moduleId) {
        ModuleSlide link = getSlideBySlideIdAndModuleId(slideId, moduleId);

        link.getSlide().setTitle(request.title());
        link.getSlide().setDescription(request.description());
        link.getSlide().setSlideType(request.slideType());
        link.getSlide().setPayload(request.payload());

        return slideMapper.toResponse(link.getSlide());
    }

    private ModuleSlide getSlideBySlideIdAndModuleId(Long slideId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return module.getSlides().stream()
                .filter(ms -> ms.getSlide().getId().equals(slideId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Slide not found in this module"));
    }
}
