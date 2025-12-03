package dev.aurivena.lms.common.api;

public record Spond<T>(
        T data,
        String error
) {
    public static <T> Spond<T> success(T data) {
        return new Spond<>(data, null);
    }

    public static <T> Spond<T> fail(String error) {
        return new Spond<>(null, error);
    }
}
