package dev.aurivena.lms.domain.slide;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.aurivena.lms.domain.course.Course;
import dev.aurivena.lms.domain.course.CourseRepository;
import dev.aurivena.lms.domain.minio.MinioService;
import dev.aurivena.lms.domain.module.Module;
import dev.aurivena.lms.domain.module.ModuleRepository;
import dev.aurivena.lms.domain.module.ModuleSlide;
import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import dev.aurivena.lms.domain.slide.dto.UpdateSlideRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Base64;
import java.util.List;


@Service
@RequiredArgsConstructor
class SlideService {
    private final SlideMapper slideMapper;
    private final SlideRepository slideRepository;
    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;
    private final MinioService minioService;
    private final ObjectMapper objectMapper;

    @Transactional
    public SlideResponse create(CreateSlideRequest request, Long moduleId, MultipartFile file) {

        JsonNode payloadToSave = downloadFile(request.payload(), request.slideType(), file);

        Slide slide = slideMapper.toEntity(
                new CreateSlideRequest(
                        request.title(),
                        request.description(),
                        request.slideType(),
                        payloadToSave
                )
        );

        slide = slideRepository.save(slide);
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("module not found"));
        ModuleSlide link = ModuleSlide.create(module, slide, module.getSlides().size() + 1);

        module.getSlides().add(link);
        moduleRepository.save(module);

        return slideMapper.toResponse(slide);
    }

    @Transactional
    public SlideResponse findById(Long slideId, long moduleId) {
        Slide slide = getSlideBySlideIdAndModuleId(slideId, moduleId).getSlide();

        if (slide.getSlideType() != SlideType.FILE) {
            return slideMapper.toResponse(slide);
        }

        JsonNode payload = slide.getPayload();
        if (payload == null || payload.isNull()) {
            throw new IllegalStateException("FILE slide without payload");
        }

        JsonNode filenameNode = payload.get("filename");
        if (filenameNode == null || filenameNode.isNull() || filenameNode.asText().isBlank()) {
            throw new IllegalStateException("FILE slide without filename in payload");
        }

        String filename = filenameNode.asText();
        byte[] body = minioService.get(filename);
        String base64 = Base64.getEncoder().encodeToString(body);

        ObjectNode enrichedPayload = (payload.isObject()
                ? ((ObjectNode) payload).deepCopy()
                : objectMapper.createObjectNode());

        enrichedPayload.put("filename", filename);
        enrichedPayload.put("contentType", "application/octet-stream");
        enrichedPayload.put("data", base64);
        return new SlideResponse(
                slide.getId(),
                slide.getTitle(),
                slide.getDescription(),
                slide.getSlideType(),
                enrichedPayload
        );
    }

    @Transactional
    public SlideResponse update(UpdateSlideRequest request, MultipartFile file, long slideId, long moduleId) {
        ModuleSlide link = getSlideBySlideIdAndModuleId(slideId, moduleId);

        link.getSlide().setTitle(request.title());
        link.getSlide().setDescription(request.description());
        link.getSlide().setSlideType(request.slideType());
        if (file != null && !file.isEmpty() && request.slideType().equals(SlideType.FILE)) {
            link.getSlide().setPayload(downloadFile(request.payload(), request.slideType(), file));
        } else {
            link.getSlide().setPayload(request.payload());
        }

        return slideMapper.toResponse(link.getSlide());
    }

    @Transactional
    public void delete(Long slideId, long moduleId, String ownerEmail) {
        Course course = courseRepository.findByModuleId(moduleId)
                .orElseThrow(() -> new RuntimeException("Этот модуль не привязан ни к одному курсу!"));

        if (!course.getOwner().getEmail().equals(ownerEmail)) {
            throw new org.springframework.security.access.AccessDeniedException("Это не ваш курс! Не трогайте слайды.");
        }

        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (!module.getSlides().removeIf(ms -> ms.getSlide().getId().equals(slideId))) {
            throw new RuntimeException("Slide not found in this module");
        }

        moduleRepository.flush();
        slideRepository.deleteById(slideId);
    }

    @SuppressWarnings("DuplicatedCode")
    @Transactional
    public void reorder(long moduleId, List<Long> newOrderIds) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        int temp = -10000;
        for (Long newOrderId : newOrderIds) {
            findLink(newOrderId, module).setIndex(temp--);
        }

        moduleRepository.flush();

        for (int i = 0; i < newOrderIds.size(); i++) {
            findLink(newOrderIds.get(i), module).setIndex(i + 1);
        }

        moduleRepository.save(module);
    }

    private ModuleSlide findLink(Long slideId, Module module) {
        return module.getSlides().stream()
                .filter(ms -> ms.getSlide().getId().equals(slideId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Slide not found in this module"));
    }

    private ModuleSlide getSlideBySlideIdAndModuleId(Long slideId, Long moduleId) {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        return module.getSlides().stream()
                .filter(ms -> ms.getSlide().getId().equals(slideId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Slide not found in this module"));
    }

    private JsonNode downloadFile(JsonNode payload, SlideType slideType, MultipartFile file) {
        JsonNode newPayload = payload;
        if (slideType == SlideType.FILE) {
            if (file == null && file.isEmpty()) {
                throw new IllegalArgumentException("file required");
            }
            String filename = minioService.upload(file);
            newPayload = objectMapper.createObjectNode()
                    .put("filename", filename);
        }
        return newPayload;
    }
}
