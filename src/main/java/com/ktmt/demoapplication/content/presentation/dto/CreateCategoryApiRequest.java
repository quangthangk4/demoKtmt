package com.ktmt.demoapplication.content.presentation.dto;

import com.ktmt.demoapplication.content.application.dto.CreateCategoryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * API Request DTO for creating a category
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API request
 * validation
 * - Separation of Concerns: API layer DTOs separated from application layer
 * DTOs
 */
@Schema(description = "Request body for creating a new category")
public record CreateCategoryApiRequest(
        @Schema(description = "Category name", example = "Programming", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Category name is required") @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters") String name,

        @Schema(description = "Category description", example = "Programming related content") String description) {
    public CreateCategoryRequest toApplicationDto() {
        return new CreateCategoryRequest(name, description);
    }
}
