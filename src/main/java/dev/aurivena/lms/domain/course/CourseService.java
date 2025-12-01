package dev.aurivena.lms.domain.course;


import dev.aurivena.lms.domain.account.Account;
import dev.aurivena.lms.domain.account.AccountService;
import dev.aurivena.lms.domain.course.dto.CourseResponse;
import dev.aurivena.lms.domain.course.dto.CreateCourseRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
class CourseService {
    private final CourseRepository courseRepository;
    private final AccountService accountService;
    private final CourseMapper courseMapper;

    @Transactional
    public CourseResponse create(CreateCourseRequest request, String ownerEmail) {
        Account owner = accountService.getAccountByEmail(ownerEmail);
        Course course = courseMapper.toEntity(request);
        course.setOwner(owner);
        return courseMapper.toResponse(courseRepository.save(course));
    }

    @Transactional
    public CourseResponse findById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        return courseMapper.toResponse(course);
    }
}
