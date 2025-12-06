package com.ktmt.demoapplication.content.presentation.dto;

import com.ktmt.demoapplication.content.application.dto.ContentResponse;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * API Response DTO for content data
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API response
 * structure
 * - Open/Closed Principle (OCP): Can be extended without modifying application
 * layer
 */
@Schema(description = "Content response data")
public record ContentApiResponse(
        @Schema(description = "Content's unique identifier", example = "c1a2b3c4-d5e6-7f8g-9h0i-1j2k3l4m5n6o") String contentId,

        @Schema(description = "Content title", example = "Introduction to Java") String title,

        @Schema(description = "Content description", example = "Learn the basics of Java programming") String description,

        @Schema(description = "Content type", example = "video", allowableValues = {
                "text", "video", "quiz", "interactive_lab" }) String type,

        @Schema(description = "Topic/Category ID", example = "550e8400-e29b-41d4-a716-446655440000") String topic,

        @Schema(description = "Creator/Author ID", example = "user123") String createdBy,

        @Schema(description = "Timestamp when the content was created", example = "2024-12-01T15:30:00") LocalDateTime createdAt,

        @Schema(description = "Timestamp when the content was last updated", example = "2024-12-01T15:30:00") LocalDateTime updatedAt){
    public static ContentApiResponse from(ContentResponse response) {
        return new ContentApiResponse(
                response.contentId(),
                response.title(),
                response.description(),
                response.type(),
                response.topic(),
                response.createdBy(),
                response.createdAt(),
                response.updatedAt());
    }
}
