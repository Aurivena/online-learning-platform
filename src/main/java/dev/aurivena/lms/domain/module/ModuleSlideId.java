package dev.aurivena.lms.domain.module;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ModuleSlideId implements Serializable {
    private long moduleID;
    private long slideID;
}
