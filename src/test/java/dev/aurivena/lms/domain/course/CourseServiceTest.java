package dev.aurivena.lms.domain.course;

import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.account.AccountService;
import dev.aurivena.lms.domain.course.dto.CourseResponse;
import dev.aurivena.lms.domain.course.dto.CreateCourseRequest;
import dev.aurivena.lms.domain.course.dto.UpdateCourseRequest;
import dev.aurivena.lms.domain.organization.Organization;
import dev.aurivena.lms.domain.organization.OrganizationRepository;
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
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private CourseMapper courseMapper;
    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private CourseService courseService;

    @Test
    void create_Success() {
        // Arrange
        CreateCourseRequest request = new CreateCourseRequest("Title", "Description", new ArrayList<>());
        Long organizationId = 1L;
        String ownerEmail = "test@example.com";
        Account owner = new Account();
        Organization organization = new Organization();
        Course course = new Course();
        CourseResponse expectedResponse = new CourseResponse(1L, "Title", "Description", new ArrayList<>());

        when(accountService.getAccountByEmail(ownerEmail)).thenReturn(owner);
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.of(organization));
        when(courseMapper.toEntity(request)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);

        // Act
        CourseResponse result = courseService.create(request, organizationId, ownerEmail);

        // Assert
        assertEquals(expectedResponse, result);
        verify(courseRepository).save(course);
        assertEquals(owner, course.getOwner());
        assertEquals(organization, course.getOrganization());
    }

    @Test
    void create_OrganizationNotFound() {
        // Arrange
        CreateCourseRequest request = new CreateCourseRequest("Title", "Description", new ArrayList<>());
        Long organizationId = 1L;
        String ownerEmail = "test@example.com";
        when(accountService.getAccountByEmail(ownerEmail)).thenReturn(new Account());
        when(organizationRepository.findById(organizationId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> courseService.create(request, organizationId, ownerEmail));
    }

    @Test
    void findById_Success() {
        // Arrange
        Long courseId = 1L;
        Long organizationId = 1L;
        Organization organization = new Organization();
        organization.setId(organizationId);
        Course course = new Course();
        course.setOrganization(organization);
        CourseResponse expectedResponse = new CourseResponse(courseId, "Title", "Description", new ArrayList<>());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);

        // Act
        CourseResponse result = courseService.findById(courseId, organizationId);

        // Assert
        assertEquals(expectedResponse, result);
    }

    @Test
    void findById_CourseNotFound() {
        // Arrange
        Long courseId = 1L;
        Long organizationId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> courseService.findById(courseId, organizationId));
    }

    @Test
    void findById_InvalidOrganization() {
        // Arrange
        Long courseId = 1L;
        Long organizationId = 1L;
        Organization organization = new Organization();
        organization.setId(2L);
        Course course = new Course();
        course.setOrganization(organization);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> courseService.findById(courseId, organizationId));
    }

    @Test
    void findAllByOrganizationId() {
        // Arrange
        Long organizationId = 1L;
        List<Course> courses = List.of(new Course());
        when(courseRepository.findAllByOrganizationId(organizationId)).thenReturn(courses);
        when(courseMapper.toResponse(any())).thenReturn(new CourseResponse(1L, "Title", "Description", new ArrayList<>()));

        // Act
        List<CourseResponse> result = courseService.findAllByOrganizationId(organizationId);

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    void update_Success() {
        // Arrange
        Long courseId = 1L;
        Long organizationId = 1L;
        UpdateCourseRequest request = new UpdateCourseRequest("New Title");
        Organization organization = new Organization();
        organization.setId(organizationId);
        Course course = new Course();
        course.setOrganization(organization);
        CourseResponse expectedResponse = new CourseResponse(courseId, "New Title", "New Desc", new ArrayList<>());

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseMapper.toResponse(course)).thenReturn(expectedResponse);

        // Act
        CourseResponse result = courseService.update(request, courseId, organizationId);

        // Assert
        assertEquals(expectedResponse, result);
        assertEquals("New Title", course.getTitle());
    }

    @Test
    void delete_Success() {
        // Arrange
        Long courseId = 1L;
        Long organizationId = 1L;
        String ownerEmail = "test@example.com";
        Organization organization = new Organization();
        organization.setId(organizationId);
        Account owner = new Account();
        owner.setEmail(ownerEmail);
        Course course = new Course();
        course.setOrganization(organization);
        course.setOwner(owner);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act
        courseService.delete(courseId, organizationId, ownerEmail);

        // Assert
        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void delete_NotOwner() {
        // Arrange
        Long courseId = 1L;
        Long organizationId = 1L;
        String ownerEmail = "test@example.com";
        Account owner = new Account();
        owner.setEmail("other@example.com");
        Course course = new Course();
        course.setOwner(owner);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> courseService.delete(courseId, organizationId, ownerEmail));
    }
}
