package com.ktmt.demoapplication.user.presentation.dto;

import com.ktmt.demoapplication.user.application.dto.UserResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * API Response DTO for user data
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API response structure
 * - Open/Closed Principle (OCP): Can be extended without modifying application layer
 */
@Schema(description = "User response data")
public record UserApiResponse(
    @Schema(description = "User's unique identifier (UUID)", example = "550e8400-e29b-41d4-a716-446655440000")
    String id,

    @Schema(description = "User's first name", example = "John")
    String firstName,

    @Schema(description = "User's last name", example = "Doe")
    String lastName,

    @Schema(description = "User's full name", example = "John Doe")
    String fullName,

    @Schema(description = "User's email address", example = "john.doe@example.com")
    String email,

    @Schema(description = "User's age", example = "30")
    Integer age,

    @Schema(description = "Whether the user is active", example = "true")
    boolean active,

    @Schema(description = "Timestamp when the user was created", example = "2024-12-01T15:30:00")
    LocalDateTime createdAt,

    @Schema(description = "Timestamp when the user was last updated", example = "2024-12-01T15:30:00")
    LocalDateTime updatedAt
) {
    public static UserApiResponse from(UserResponse response) {
        return new UserApiResponse(
            response.id(),
            response.firstName(),
            response.lastName(),
            response.fullName(),
            response.email(),
            response.age(),
            response.active(),
            response.createdAt(),
            response.updatedAt()
        );
    }
}
