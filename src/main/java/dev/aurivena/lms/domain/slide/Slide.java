package dev.aurivena.lms.domain.slide;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.time.Instant;

@Entity
@Table(name = "slides")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Slide {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(nullable = false)
    @JdbcType(PostgreSQLEnumJdbcType.class)
    private SlideType slideType;


    @OneToOne(fetch = FetchType.LAZY)
    @JoinTable(
            name = "slide_payload",
            joinColumns = @JoinColumn(name = "slide_id"),
            inverseJoinColumns = @JoinColumn(name = "id")
    )
    private Payload payload;

    @Column(updatable = false)
    private Instant createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = Instant.now();
    }
}
