package dev.aurivena.lms.domain.course;

import dev.aurivena.lms.domain.module.Module;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "course_modules")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class CourseModule {

    @EmbeddedId
    private CourseModuleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("courseId")
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("moduleId")
    @JoinColumn(name = "module_id")
    private Module module;

    @Column(name = "index")
    private int index;

    public static CourseModule create(Course course, Module module, int index) {
        CourseModule link = new CourseModule();
        link.setId(new CourseModuleId(course.getId(), module.getId()));
        link.setCourse(course);
        link.setModule(module);
        link.setIndex(index);
        return link;
    }
}