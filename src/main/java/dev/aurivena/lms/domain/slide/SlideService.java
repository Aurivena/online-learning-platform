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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Objects;


@Slf4j
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

        JsonNode payloadToSave = uploadFile(request.payload(), request.slideType(), file);

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
        Slide slide = getSlideBySlideIdAndModuleId(slideId, moduleId);

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
    public SlideResponse update(UpdateSlideRequest request, MultipartFile file, long slideId, long moduleId) throws IOException {
        Slide slide = getSlideBySlideIdAndModuleId(slideId, moduleId);

        String oldFileName = null;
        if (slide.getPayload() != null && slide.getPayload().has("filename")) {
            oldFileName = slide.getPayload().get("filename").asText();
        }

        boolean fileWasUpdated = false;
        JsonNode newPayload = request.payload();

        if (file != null && !file.isEmpty() && request.slideType() == SlideType.FILE) {
            newPayload = uploadFile(request.payload(), request.slideType(), file);
            fileWasUpdated = true;
        }

        slide.setTitle(request.title());
        slide.setDescription(request.description());
        slide.setSlideType(request.slideType());
        slide.setPayload(newPayload);

        if (fileWasUpdated && oldFileName != null) {
            try {
                minioService.delete(oldFileName);
            } catch (Exception e) {
                log.warn("Error at delete: {}", oldFileName);
            }
        }


        return slideMapper.toResponse(slide);
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

    @Transactional
    public Boolean checkOption(long slideId, long moduleId, Long selectedOptionId) {
        Slide slide = getSlideBySlideIdAndModuleId(slideId, moduleId);
        JsonNode payload = slide.getPayload();

        if (payload == null || payload.get("output") == null) {
            throw new RuntimeException("Test payload invalid");
        }

        Long outputId = payload.get("output").asLong();

        if (!outputId.equals(selectedOptionId)) {
            updateIsRight(slide,false);
            return false;
        }

        updateIsRight(slide,true);
        return true;
    }

    private ModuleSlide findLink(Long slideId, Module module) {
        return module.getSlides().stream()
                .filter(ms -> ms.getSlide().getId().equals(slideId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Slide not found in this module"));
    }

    private Slide getSlideBySlideIdAndModuleId(Long slideId, Long moduleId) {
        Module module = moduleRepository.findByIdWithSlides(moduleId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        ModuleSlide moduleSlide = module.getSlides().stream()
                .filter(ms -> ms.getSlide().getId().equals(slideId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Slide not found in this module"));
        return moduleSlide.getSlide();
    }

    private JsonNode uploadFile(JsonNode payload, SlideType slideType, MultipartFile file) {
        JsonNode newPayload = payload;
        String filename = null;
        if (slideType == SlideType.FILE) {
            if (file == null && file.isEmpty()) {
                throw new IllegalArgumentException("file required");
            }
            try {
                filename = minioService.upload(file);
            } catch (Exception e) {
                log.warn("error at upload file: {}", e.getMessage());
            }
            newPayload = objectMapper.createObjectNode()
                    .put("filename", filename);
        }
        return newPayload;
    }

    private void updateIsRight(Slide slide, Boolean right) {
        JsonNode payload = slide.getPayload();

        if (!(payload instanceof ObjectNode objectNode)) {
            throw new IllegalStateException("Payload is not ObjectNode");
        }

        objectNode.put("is_right", right);

        slide.setPayload(objectNode);
    }
}
