package dev.aurivena.lms.domain.account;

import java.time.Instant;
import java.util.Date;

public enum JwtType {
    ACCESS(15 * 60 * 1000L),
    REFRESH(7L * 24 * 60 * 60 * 1000L);

    private final long value;

    JwtType(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}