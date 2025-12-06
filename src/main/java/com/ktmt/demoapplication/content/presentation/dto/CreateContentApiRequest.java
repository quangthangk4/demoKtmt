package com.ktmt.demoapplication.content.presentation.dto;

import com.ktmt.demoapplication.content.application.dto.CreateContentRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * API Request DTO for creating content
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API request
 * validation
 * - Separation of Concerns: API layer DTOs separated from application layer
 * DTOs
 */
@Schema(description = "Request body for creating new content")
public record CreateContentApiRequest(
        @Schema(description = "Content title", example = "Introduction to Java", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Title is required") @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters") String title,

        @Schema(description = "Content description", example = "Learn the basics of Java programming") String description,

        @Schema(description = "Content type", example = "video", allowableValues = {
                "text", "video", "quiz",
                "interactive_lab" }, requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Type is required") @Pattern(regexp = "^(text|video|quiz|interactive_lab)$", message = "Type must be one of: text, video, quiz, interactive_lab") String type,

        @Schema(description = "Topic/Category ID (UUID format)", example = "550e8400-e29b-41d4-a716-446655440000", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Topic is required") @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "Topic must be a valid UUID") String topic,

        @Schema(description = "Creator/Author ID", example = "user123", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "CreatedBy is required") String createdBy){
    public CreateContentRequest toApplicationDto() {
        return new CreateContentRequest(title, description, type, topic, createdBy);
    }
}
