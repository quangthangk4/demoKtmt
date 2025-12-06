package com.ktmt.demoapplication.content.presentation.controller;

import com.ktmt.demoapplication.content.application.dto.ContentResponse;
import com.ktmt.demoapplication.content.application.usecase.CreateContentUseCase;
import com.ktmt.demoapplication.content.application.usecase.DeleteContentUseCase;
import com.ktmt.demoapplication.content.application.usecase.GetContentUseCase;
import com.ktmt.demoapplication.content.application.usecase.UpdateContentUseCase;
import com.ktmt.demoapplication.content.presentation.dto.ContentApiResponse;
import com.ktmt.demoapplication.content.presentation.dto.CreateContentApiRequest;
import com.ktmt.demoapplication.content.presentation.dto.UpdateContentApiRequest;
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

/**
 * REST Controller for Content API
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only handles HTTP requests/responses
 * - Dependency Inversion Principle (DIP): Depends on use case abstractions
 * - Open/Closed Principle (OCP): Can add new endpoints without modifying
 * existing ones
 * - Interface Segregation Principle (ISP): Uses specific use cases instead of
 * one large service
 */
@Tag(name = "Content Management", description = "APIs for managing learning content")
@RestController
@RequestMapping("/api/v1/content")
public class ContentController {

    private final CreateContentUseCase createContentUseCase;
    private final GetContentUseCase getContentUseCase;
    private final UpdateContentUseCase updateContentUseCase;
    private final DeleteContentUseCase deleteContentUseCase;

    public ContentController(
            CreateContentUseCase createContentUseCase,
            GetContentUseCase getContentUseCase,
            UpdateContentUseCase updateContentUseCase,
            DeleteContentUseCase deleteContentUseCase) {
        this.createContentUseCase = createContentUseCase;
        this.getContentUseCase = getContentUseCase;
        this.updateContentUseCase = updateContentUseCase;
        this.deleteContentUseCase = deleteContentUseCase;
    }

    @Operation(summary = "Create new content", description = "Creates new content with the provided information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Content created successfully", content = @Content(schema = @Schema(implementation = ContentApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "404", description = "Topic/Category not found")
    })
    @PostMapping
    public ResponseEntity<ApiResponseData<ContentApiResponse>> createContent(
            @Valid @RequestBody CreateContentApiRequest request) {
        ContentResponse response = createContentUseCase.execute(request.toApplicationDto());
        ContentApiResponse apiResponse = ContentApiResponse.from(response);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponseData.success("Content created successfully", apiResponse));
    }

    @Operation(summary = "Get all content", description = "Retrieves a list of all content")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of content retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseData<List<ContentApiResponse>>> getAllContent() {
        List<ContentResponse> responses = getContentUseCase.getAllContent();

        List<ContentApiResponse> apiResponses = responses.stream()
                .map(ContentApiResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponseData.success(apiResponses));
    }

    @Operation(summary = "Search content", description = "Searches content based on search conditions")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Search results retrieved successfully")
    })
    @GetMapping("/search")
    public ResponseEntity<ApiResponseData<List<ContentApiResponse>>> searchContent(
            @Parameter(description = "Search condition (keyword to search in title, description, etc.)") @RequestParam(value = "cond", required = false, defaultValue = "") String cond) {
        List<ContentResponse> responses = getContentUseCase.searchContent(cond);

        List<ContentApiResponse> apiResponses = responses.stream()
                .map(ContentApiResponse::from)
                .toList();

        return ResponseEntity.ok(ApiResponseData.success(apiResponses));
    }

    @Operation(summary = "Get content by ID", description = "Retrieves content by its unique identifier")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content found", content = @Content(schema = @Schema(implementation = ContentApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid content ID format"),
            @ApiResponse(responseCode = "404", description = "Content not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseData<ContentApiResponse>> getContentById(
            @Parameter(description = "Content ID", required = true) @PathVariable String id) {
        ContentResponse response = getContentUseCase.getContentById(id);
        ContentApiResponse apiResponse = ContentApiResponse.from(response);
        return ResponseEntity.ok(ApiResponseData.success(apiResponse));
    }

    @Operation(summary = "Update content", description = "Updates existing content's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
            @ApiResponse(responseCode = "404", description = "Content or Topic not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> updateContent(
            @Parameter(description = "Content ID", required = true) @PathVariable String id,
            @Valid @RequestBody UpdateContentApiRequest request) {
        updateContentUseCase.execute(id, request.toApplicationDto());
        return ResponseEntity.ok(ApiResponseData.success("Content updated successfully", null));
    }

    @Operation(summary = "Delete content", description = "Deletes content from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Content deleted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid content ID format"),
            @ApiResponse(responseCode = "404", description = "Content not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> deleteContent(
            @Parameter(description = "Content ID", required = true) @PathVariable String id) {
        deleteContentUseCase.execute(id);
        return ResponseEntity.ok(ApiResponseData.success("Content deleted successfully", null));
    }
}
