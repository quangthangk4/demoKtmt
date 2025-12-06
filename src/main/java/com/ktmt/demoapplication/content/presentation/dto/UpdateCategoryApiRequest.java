package com.ktmt.demoapplication.content.presentation.dto;

import com.ktmt.demoapplication.content.application.dto.UpdateCategoryRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * API Request DTO for updating a category
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for API request
 * validation
 */
@Schema(description = "Request body for updating a category")
public record UpdateCategoryApiRequest(
        @Schema(description = "Category name", example = "Programming", requiredMode = Schema.RequiredMode.REQUIRED) @NotBlank(message = "Category name is required") @Size(min = 1, max = 100, message = "Category name must be between 1 and 100 characters") String name,

        @Schema(description = "Category description", example = "Programming related content") String description) {
    public UpdateCategoryRequest toApplicationDto() {
        return new UpdateCategoryRequest(name, description);
    }
}
