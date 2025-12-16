package dev.aurivena.lms.domain.quetion;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "questions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question {
    @Id
    private Long id;
    private String text;
    private List<Option> options;
    private String output;
}
