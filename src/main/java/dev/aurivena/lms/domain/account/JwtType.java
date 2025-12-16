package dev.aurivena.lms.domain.account;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtType {
    ACCESS(24 * 60 * 60 * 1000L),
    REFRESH(7L * 24 * 60 * 60 * 1000L);

    private final long duration;

}