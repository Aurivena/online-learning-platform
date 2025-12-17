package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.course.Course;
import dev.aurivena.lms.domain.course.CourseModule;
import dev.aurivena.lms.domain.course.CourseRepository;
import dev.aurivena.lms.domain.module.dto.CreateModuleRequest;
import dev.aurivena.lms.domain.module.dto.ModuleResponse;
import dev.aurivena.lms.domain.module.dto.UpdateModuleRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private ModuleRepository moduleRepository;
    @Mock
    private ModuleMapper moduleMapper;
    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ModuleService moduleService;

    @Test
    void create_Success() {
        // Arrange
        CreateModuleRequest request = new CreateModuleRequest("Title", new ArrayList<>());
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setModules(new ArrayList<>());
        Module module = new Module();
        module.setId(1L);
        ModuleResponse expectedResponse = new ModuleResponse(1L, "Title", new ArrayList<>());

        when(moduleMapper.toEntity(request)).thenReturn(module);
        when(moduleRepository.save(module)).thenReturn(module);
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(moduleMapper.toResponse(module)).thenReturn(expectedResponse);

        // Act
        ModuleResponse result = moduleService.create(request, courseId);

        // Assert
        assertEquals(expectedResponse, result);
        assertEquals(1, course.getModules().size());
        verify(courseRepository).save(course);
    }

    @Test
    void create_CourseNotFound() {
        // Arrange
        CreateModuleRequest request = new CreateModuleRequest("Title", new ArrayList<>());
        Long courseId = 1L;
        when(moduleMapper.toEntity(request)).thenReturn(new Module());
        when(moduleRepository.save(any())).thenReturn(new Module());
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> moduleService.create(request, courseId));
    }

    @Test
    void findById_Success() {
        // Arrange
        Long moduleId = 1L;
        Long courseId = 1L;
        Course course = new Course();
        Module module = new Module();
        module.setId(moduleId);
        CourseModule courseModule = CourseModule.create(course, module, 1);
        course.setModules(List.of(courseModule));
        ModuleResponse expectedResponse = new ModuleResponse(moduleId, "Title", new ArrayList<>());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(moduleMapper.toResponse(module)).thenReturn(expectedResponse);

        // Act
        ModuleResponse result = moduleService.findById(moduleId, courseId);

        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    void findById_ModuleNotFound() {
        // Arrange
        Long moduleId = 1L;
        Long courseId = 1L;
        Course course = new Course();
        course.setModules(new ArrayList<>());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> moduleService.findById(moduleId, courseId));
    }

    @Test
    void update_Success() {
        // Arrange
        Long moduleId = 1L;
        Long courseId = 1L;
        UpdateModuleRequest request = new UpdateModuleRequest("New Title", 1);
        Course course = new Course();
        Module module = new Module();
        module.setId(moduleId);
        CourseModule courseModule = CourseModule.create(course, module, 1);
        course.setModules(List.of(courseModule));
        ModuleResponse expectedResponse = new ModuleResponse(moduleId, "New Title", new ArrayList<>());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(moduleRepository.findById(moduleId)).thenReturn(Optional.of(module));
        when(moduleMapper.toResponse(module)).thenReturn(expectedResponse);

        // Act
        ModuleResponse result = moduleService.update(request, moduleId, courseId);

        // Assert
        assertEquals(expectedResponse, result);
        assertEquals("New Title", module.getTitle());
    }

    @Test
    void delete_Success() {
        // Arrange
        Long moduleId = 1L;
        Long courseId = 1L;
        String ownerEmail = "test@example.com";
        Course course = new Course();
        Account owner = new Account();
        owner.setEmail(ownerEmail);
        course.setOwner(owner);
        Module module = new Module();
        module.setId(moduleId);
        CourseModule courseModule = CourseModule.create(course, module, 1);
        // Use a mutable list
        course.setModules(new ArrayList<>(List.of(courseModule)));

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        moduleService.delete(moduleId, courseId, ownerEmail);

        // Assert
        verify(moduleRepository).deleteById(moduleId);
        assertTrue(course.getModules().isEmpty());
    }

    @Test
    void delete_NotOwner() {
        // Arrange
        Long moduleId = 1L;
        Long courseId = 1L;
        String ownerEmail = "test@example.com";
        Course course = new Course();
        Account owner = new Account();
        owner.setEmail("other@example.com");
        course.setOwner(owner);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> moduleService.delete(moduleId, courseId, ownerEmail));
    }

    @Test
    void reorder_Success() {
        // Arrange
        Long courseId = 1L;
        Course course = new Course();
        Module module1 = new Module(); module1.setId(1L);
        Module module2 = new Module(); module2.setId(2L);

        CourseModule cm1 = CourseModule.create(course, module1, 1);
        CourseModule cm2 = CourseModule.create(course, module2, 2);

        course.setModules(List.of(cm1, cm2));

        List<Long> newOrderIds = List.of(2L, 1L);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        moduleService.reorder(courseId, newOrderIds);

        // Assert
        verify(courseRepository).save(course);
        assertEquals(1, cm2.getIndex());
        assertEquals(2, cm1.getIndex());
    }
}
