package com.ktmt.demoapplication.content.presentation.controller;

import com.ktmt.demoapplication.content.application.dto.CategoryResponse;
import com.ktmt.demoapplication.content.application.usecase.CreateCategoryUseCase;
import com.ktmt.demoapplication.content.application.usecase.DeleteCategoryUseCase;
import com.ktmt.demoapplication.content.application.usecase.GetCategoryUseCase;
import com.ktmt.demoapplication.content.application.usecase.UpdateCategoryUseCase;
import com.ktmt.demoapplication.content.presentation.dto.CategoryApiResponse;
import com.ktmt.demoapplication.content.presentation.dto.CreateCategoryApiRequest;
import com.ktmt.demoapplication.content.presentation.dto.UpdateCategoryApiRequest;
import com.ktmt.demoapplication.user.presentation.dto.ApiResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST Controller for Category API
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only handles HTTP requests/responses
 * - Dependency Inversion Principle (DIP): Depends on use case abstractions
 * - Open/Closed Principle (OCP): Can add new endpoints without modifying
 * existing ones
 * - Interface Segregation Principle (ISP): Uses specific use cases instead of
 * one large service
 */
@Tag(name = "Category Management", description = "APIs for managing content categories")
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryUseCase getCategoryUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;

    public CategoryController(
            CreateCategoryUseCase createCategoryUseCase,
            GetCategoryUseCase getCategoryUseCase,
            UpdateCategoryUseCase updateCategoryUseCase,
            DeleteCategoryUseCase deleteCategoryUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryUseCase = getCategoryUseCase;
        this.updateCategoryUseCase = updateCategoryUseCase;
        this.deleteCategoryUseCase = deleteCategoryUseCase;
    }

    @Operation(summary = "Create a new category", description = "Creates a new category with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Category created successfully", content = @Content(schema = @Schema(implementation = CategoryApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error")
    })
    @PostMapping
    public ResponseEntity<ApiResponseData<CategoryApiResponse>> createCategory(
            @Valid @RequestBody CreateCategoryApiRequest request) {
        CategoryResponse response = createCategoryUseCase.execute(request.toApplicationDto());
        CategoryApiResponse apiResponse = CategoryApiResponse.from(response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseData.success("Category created successfully", apiResponse));
    }

    @Operation(summary = "Get all categories", description = "Retrieves a list of all categories")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of categories retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseData<List<CategoryApiResponse>>> getAllCategories() {
        List<CategoryResponse> responses = getCategoryUseCase.getAllCategory();

        List<CategoryApiResponse> apiResponses = responses.stream()
                .map(CategoryApiResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponseData.success(apiResponses));
    }

    @Operation(summary = "Get category by ID", description = "Retrieves a category by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category found", content = @Content(schema = @Schema(implementation = CategoryApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid category ID format"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseData<CategoryApiResponse>> getCategoryById(
            @Parameter(description = "Category ID (UUID format)", required = true) @PathVariable String id) {
        UUID categoryId = UUID.fromString(id);
        CategoryResponse response = getCategoryUseCase.getCategoryById(categoryId);
        CategoryApiResponse apiResponse = CategoryApiResponse.from(response);
        return ResponseEntity.ok(ApiResponseData.success(apiResponse));
    }

    @Operation(summary = "Update category", description = "Updates an existing category's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> updateCategory(
            @Parameter(description = "Category ID (UUID format)", required = true) @PathVariable String id,
            @Valid @RequestBody UpdateCategoryApiRequest request) {
        UUID categoryId = UUID.fromString(id);
        updateCategoryUseCase.execute(categoryId, request.toApplicationDto());
        return ResponseEntity.ok(ApiResponseData.success("Category updated successfully", null));
    }

    @Operation(summary = "Delete category", description = "Deletes a category from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Category deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid category ID format"),
            @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> deleteCategory(
            @Parameter(description = "Category ID (UUID format)", required = true) @PathVariable String id) {
        UUID categoryId = UUID.fromString(id);
        deleteCategoryUseCase.execute(categoryId);
        return ResponseEntity.ok(ApiResponseData.success("Category deleted successfully", null));
    }
}
