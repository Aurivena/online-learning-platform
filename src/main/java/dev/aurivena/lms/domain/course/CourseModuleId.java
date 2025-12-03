package dev.aurivena.lms.domain.course;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CourseModuleId implements Serializable {
    private Long courseId;
    private Long moduleId;
}
