package dev.aurivena.lms.domain.slide;

import com.fasterxml.jackson.databind.JsonNode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "slide_payload")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class Payload {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "slide_id")
    private Slide slide;

    @JdbcTypeCode(SqlTypes.JSON)
    JsonNode text;
    @JdbcTypeCode(SqlTypes.JSON)
    JsonNode output;

    public static Payload create(Slide slide,JsonNode text,JsonNode output) {
        Payload link = new Payload();
        link.slide = slide;
        link.text = text;
        link.output = output;
        return link;
    }
}
