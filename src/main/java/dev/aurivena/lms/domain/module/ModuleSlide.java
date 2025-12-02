package dev.aurivena.lms.domain.module;

import dev.aurivena.lms.domain.slide.Slide;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "module_slides")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
public class ModuleSlide {
    @EmbeddedId
    private ModuleSlideId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("moduleID")
    @JoinColumn(name = "module_id")
    private Module module;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("slideID")
    @JoinColumn(name = "slide_id")
    private Slide slide;

    @Column(name = "index")
    private long index;

    public static ModuleSlide create(Module module, Slide slide, int index) {
        ModuleSlide link = new ModuleSlide();
        link.setId(new ModuleSlideId(module.getId(), slide.getId()));
        link.setModule(module);
        link.setSlide(slide);
        link.setIndex(index);
        return link;
    }
}
