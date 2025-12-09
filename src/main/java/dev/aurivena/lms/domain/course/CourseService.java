package dev.aurivena.lms.domain.course;


import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.account.AccountService;
import dev.aurivena.lms.domain.course.dto.CourseResponse;
import dev.aurivena.lms.domain.course.dto.CreateCourseRequest;
import dev.aurivena.lms.domain.course.dto.UpdateCourseRequest;
import dev.aurivena.lms.domain.organization.Organization;
import dev.aurivena.lms.domain.organization.OrganizationRepository;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final AccountService accountService;
    private final CourseMapper courseMapper;
    private final OrganizationRepository organizationRepository;

    @Transactional
    public CourseResponse create(CreateCourseRequest request, Long organizationId, String ownerEmail) {
        Account owner = accountService.getAccountByEmail(ownerEmail);
        Organization organization = organizationRepository.findById(organizationId)
                .orElseThrow(() -> new RuntimeException("Organization not found"));
        Course course = courseMapper.toEntity(request);
        course.setOrganization(organization);
        course.setOwner(owner);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse findById(Long courseId, Long organizationId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (notValidCourseOrganization(course, organizationId)) {
            throw new RuntimeException("Organization id not found");
        }

        return courseMapper.toResponse(course);
    }

    @Transactional
    public List<CourseResponse> findAllByOrganizationId(Long organizationId) {
        return courseRepository.findAllByOrganizationId(organizationId)
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Transactional
    public CourseResponse update(UpdateCourseRequest request, Long courseId, Long organizationId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (notValidCourseOrganization(course, organizationId)) {
            throw new RuntimeException("Organization id not found");
        }

        course.setTitle(request.title());

        return courseMapper.toResponse(course);
    }

    @Transactional
    public void delete(Long courseId, Long organizationId, String ownerEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (!course.getOwner().getEmail().equals(ownerEmail)) {
            throw new org.springframework.security.access.AccessDeniedException("Это не ваш курс! Не трогайте слайды.");
        }

        if (notValidCourseOrganization(course, organizationId)) {
            throw new RuntimeException("Organization id not found");
        }

        courseRepository.deleteById(courseId);
    }

    private boolean notValidCourseOrganization(Course course, Long organizationId) {


        return !course.getOrganization().getId().equals(organizationId);
    }
}
