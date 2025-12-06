package com.ktmt.demoapplication.content.presentation.dto;

import com.ktmt.demoapplication.content.application.dto.UpdateContentRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * API Request DTO for updating content
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API request
 * validation
 */
@Schema(description = "Request body for updating content")
public record UpdateContentApiRequest(
        @Schema(description = "Content title", example = "Introduction to Java", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Title is required") @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters") String title,

        @Schema(description = "Content description", example = "Learn the basics of Java programming") String description,

        @Schema(description = "Topic/Category ID (UUID format)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Topic is required") @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "Topic must be a valid UUID") String topic) {
    public UpdateContentRequest toApplicationDto() {
        return new UpdateContentRequest(title, description, topic);
    }
}
