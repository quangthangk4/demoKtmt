package com.ktmt.demoapplication.content.presentation.dto;

import com.ktmt.demoapplication.content.application.dto.CategoryResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * API Response DTO for category data
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API response
 * structure
 * - Open/Closed Principle (OCP): Can be extended without modifying application
 * layer
 */
@Schema(description = "Category response data")
public record CategoryApiResponse(
        @Schema(description = "Category's unique identifier (UUID)", example = "550e8400-e29b-41d4-a716-446655440000") String categoryId,

        @Schema(description = "Category name", example = "Programming") String name,

        @Schema(description = "Category description", example = "Programming related content") String description,

        @Schema(description = "Timestamp when the category was created", example = "2024-12-01T15:30:00") LocalDateTime createdAt,

        @Schema(description = "Timestamp when the category was last updated", example = "2024-12-01T15:30:00") LocalDateTime updatedAt) {
    public static CategoryApiResponse from(CategoryResponse response) {
        return new CategoryApiResponse(
                response.categoryId(),
                response.name(),
                response.description(),
                response.createdAt(),
                response.updatedAt());
    }
}
