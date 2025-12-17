package dev.aurivena.lms.domain.slide;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.course.Course;
import dev.aurivena.lms.domain.course.CourseRepository;
import dev.aurivena.lms.domain.minio.MinioService;
import dev.aurivena.lms.domain.module.Module;
import dev.aurivena.lms.domain.module.ModuleRepository;
import dev.aurivena.lms.domain.module.ModuleSlide;
import dev.aurivena.lms.domain.slide.dto.CreateSlideRequest;
import dev.aurivena.lms.domain.slide.dto.SlideResponse;
import dev.aurivena.lms.domain.slide.dto.UpdateSlideRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SlideServiceTest {

    @Mock
    private SlideMapper slideMapper;
    @Mock
    private SlideRepository slideRepository;
    @Mock
    private ModuleRepository moduleRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private MinioService minioService;
    @Spy
    private ObjectMapper objectMapper = new ObjectMapper();

    @InjectMocks
    private SlideService slideService;

    @Test
    void create_Success() {
        // Arrange
        CreateSlideRequest request = new CreateSlideRequest("Title", "Desc", SlideType.TEXT, objectMapper.createObjectNode());
        Long moduleId = 1L;
        Module module = new Module();
        module.setId(moduleId);
        module.setSlides(new ArrayList<>());
        Slide slide = new Slide();
        slide.setId(1L);
        SlideResponse expectedResponse = new SlideResponse(1L, "Title", "Desc", SlideType.TEXT, objectMapper.createObjectNode());

        when(slideMapper.toEntity(any())).thenReturn(slide);
        when(slideRepository.save(slide)).thenReturn(slide);
        when(moduleRepository.findById(moduleId)).thenReturn(Optional.of(module));
        when(slideMapper.toResponse(slide)).thenReturn(expectedResponse);

        // Act
        SlideResponse result = slideService.create(request, moduleId, null);

        // Assert
        assertEquals(expectedResponse, result);
        assertEquals(1, module.getSlides().size());
        verify(moduleRepository).save(module);
    }

    @Test
    void create_WithFile() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "content".getBytes());
        CreateSlideRequest request = new CreateSlideRequest("Title", "Desc", SlideType.FILE, objectMapper.createObjectNode());
        Long moduleId = 1L;
        Module module = new Module();
        module.setId(moduleId);
        module.setSlides(new ArrayList<>());
        Slide slide = new Slide();
        slide.setId(1L);
        SlideResponse expectedResponse = new SlideResponse(1L, "Title", "Desc", SlideType.FILE, objectMapper.createObjectNode().put("filename", "uuid"));

        when(minioService.upload(file)).thenReturn("uuid");
        when(slideMapper.toEntity(any())).thenReturn(slide);
        when(slideRepository.save(slide)).thenReturn(slide);
        when(moduleRepository.findById(moduleId)).thenReturn(Optional.of(module));
        when(slideMapper.toResponse(slide)).thenReturn(expectedResponse);

        // Act
        SlideResponse result = slideService.create(request, moduleId, file);

        // Assert
        assertEquals(expectedResponse, result);
        verify(minioService).upload(file);
    }

    @Test
    void findById_Success_Text() {
        // Arrange
        Long slideId = 1L;
        Long moduleId = 1L;
        Module module = new Module();
        module.setId(moduleId);
        Slide slide = new Slide();
        slide.setId(slideId);
        slide.setSlideType(SlideType.TEXT);
        ModuleSlide moduleSlide = ModuleSlide.create(module, slide, 1);
        module.setSlides(List.of(moduleSlide));
        SlideResponse expectedResponse = new SlideResponse(slideId, "Title", "Desc", SlideType.TEXT, objectMapper.createObjectNode());

        when(moduleRepository.findByIdWithSlides(moduleId)).thenReturn(Optional.of(module));
        when(slideMapper.toResponse(slide)).thenReturn(expectedResponse);

        // Act
        SlideResponse result = slideService.findById(slideId, moduleId);

        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    void update_Success() throws IOException {
        // Arrange
        Long slideId = 1L;
        Long moduleId = 1L;
        UpdateSlideRequest request = new UpdateSlideRequest("New Title", "New Desc", SlideType.TEXT, objectMapper.createObjectNode());
        Module module = new Module();
        module.setId(moduleId);
        Slide slide = new Slide();
        slide.setId(slideId);
        ModuleSlide moduleSlide = ModuleSlide.create(module, slide, 1);
        module.setSlides(List.of(moduleSlide));
        SlideResponse expectedResponse = new SlideResponse(slideId, "New Title", "New Desc", SlideType.TEXT, objectMapper.createObjectNode());

        when(moduleRepository.findByIdWithSlides(moduleId)).thenReturn(Optional.of(module));
        when(slideMapper.toResponse(slide)).thenReturn(expectedResponse);

        // Act
        SlideResponse result = slideService.update(request, null, slideId, moduleId);

        // Assert
        assertEquals(expectedResponse, result);
        assertEquals("New Title", slide.getTitle());
    }

    @Test
    void delete_Success() {
        // Arrange
        Long slideId = 1L;
        Long moduleId = 1L;
        String ownerEmail = "test@example.com";
        Course course = new Course();
        Account owner = new Account();
        owner.setEmail(ownerEmail);
        course.setOwner(owner);
        Module module = new Module();
        module.setId(moduleId);
        Slide slide = new Slide();
        slide.setId(slideId);
        ModuleSlide moduleSlide = ModuleSlide.create(module, slide, 1);
        module.setSlides(new ArrayList<>(List.of(moduleSlide)));

        when(courseRepository.findByModuleId(moduleId)).thenReturn(Optional.of(course));
        when(moduleRepository.findById(moduleId)).thenReturn(Optional.of(module));

        // Act
        slideService.delete(slideId, moduleId, ownerEmail);

        // Assert
        verify(slideRepository).deleteById(slideId);
        assertTrue(module.getSlides().isEmpty());
    }

    @Test
    void checkOption_Correct() {
        // Arrange
        Long slideId = 1L;
        Long moduleId = 1L;
        Long correctOptionId = 1L;

        Module module = new Module();
        module.setId(moduleId);
        Slide slide = new Slide();
        slide.setId(slideId);
        ObjectNode payload = objectMapper.createObjectNode();
        payload.put("output", correctOptionId);
        slide.setPayload(payload);

        ModuleSlide moduleSlide = ModuleSlide.create(module, slide, 1);
        // Use mutable list for slides in module because checkOption might modify state indirectly or just good practice
        module.setSlides(new ArrayList<>(List.of(moduleSlide)));

        when(moduleRepository.findByIdWithSlides(moduleId)).thenReturn(Optional.of(module));

        // Act
        Boolean result = slideService.checkOption(slideId, moduleId, correctOptionId);

        // Assert
        assertTrue(result);
        assertTrue(slide.getPayload().get("is_right").asBoolean());
    }
}
