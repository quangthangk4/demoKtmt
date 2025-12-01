package com.ktmt.demoapplication.user.presentation.dto;

/**
 * Generic API Response wrapper
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Wraps API responses with consistent structure
 * - Open/Closed Principle (OCP): Generic type allows reuse for any response type
 */
public record ApiResponseData<T>(
    boolean success,
    String message,
    T data
) {
    public static <T> ApiResponseData<T> success(T data) {
        return new ApiResponseData<>(true, "Success", data);
    }

    public static <T> ApiResponseData<T> success(String message, T data) {
        return new ApiResponseData<>(true, message, data);
    }

    public static <T> ApiResponseData<T> error(String message) {
        return new ApiResponseData<>(false, message, null);
    }
}
