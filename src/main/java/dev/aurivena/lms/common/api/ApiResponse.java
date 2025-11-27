package dev.aurivena.lms.common.api;

public record ApiResponse<T>(
        T data,
        String error
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, null);
    }

    public static <T> ApiResponse<T> fail(String error) {
        return new ApiResponse<>(null, error);
    }
}
