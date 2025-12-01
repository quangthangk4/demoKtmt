package com.ktmt.demoapplication.user.presentation.dto;

/**
 * Generic API Response wrapper
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Wraps API responses with consistent structure
 * - Open/Closed Principle (OCP): Generic type allows reuse for any response type
 */
public record ApiResponse<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}
