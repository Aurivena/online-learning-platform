package dev.aurivena.lms.domain.slide;
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

    @Transactional
    public SlideResponse create(CreateSlideRequest request) {
        return slideMapper.toResponse(slideRepository.save(slideMapper.toEntity(request)));
    }

    @Transactional
    public SlideResponse findById(Long id){
        Slide slide = slideRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Slide not found"));
        return slideMapper.toResponse(slide);
    }
}
