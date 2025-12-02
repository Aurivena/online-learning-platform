package dev.aurivena.lms.domain.slide;
import dev.aurivena.lms.domain.module.Module;
import dev.aurivena.lms.domain.module.ModuleRepository;
import dev.aurivena.lms.domain.module.ModuleSlide;
import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
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
    public SlideResponse create(CreateSlideRequest request) {
        return slideMapper.toResponse(slideRepository.save(slideMapper.toEntity(request)));
    }

    @Transactional
    public SlideResponse findById(Long slideId, long moduleId){
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        ModuleSlide link = module.getSlides().stream()
                .filter(ms -> ms.getSlide().getId().equals(slideId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Slide not found in this module"));
        return slideMapper.toResponse(link.getSlide());
    }
}
