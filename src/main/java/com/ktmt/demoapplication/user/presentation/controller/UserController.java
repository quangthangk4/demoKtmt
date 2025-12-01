package com.ktmt.demoapplication.user.presentation.controller;

import com.ktmt.demoapplication.user.application.dto.UserResponse;
import com.ktmt.demoapplication.user.application.usecase.CreateUserUseCase;
import com.ktmt.demoapplication.user.application.usecase.DeleteUserUseCase;
import com.ktmt.demoapplication.user.application.usecase.GetUserUseCase;
import com.ktmt.demoapplication.user.application.usecase.UpdateUserUseCase;
import com.ktmt.demoapplication.user.presentation.dto.ApiResponseData;
import com.ktmt.demoapplication.user.presentation.dto.CreateUserApiRequest;
import com.ktmt.demoapplication.user.presentation.dto.UpdateUserApiRequest;
import com.ktmt.demoapplication.user.presentation.dto.UserApiResponse;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for User API
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only handles HTTP requests/responses
 * - Dependency Inversion Principle (DIP): Depends on use case abstractions
 * - Open/Closed Principle (OCP): Can add new endpoints without modifying existing ones
 * - Interface Segregation Principle (ISP): Uses specific use cases instead of one large service
 */
@Tag(name = "User Management", description = "APIs for managing users")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserUseCase getUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;

    public UserController(
            CreateUserUseCase createUserUseCase,
            GetUserUseCase getUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase) {
        this.createUserUseCase = createUserUseCase;
        this.getUserUseCase = getUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
    }

    @Operation(summary = "Create a new user", description = "Creates a new user with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully",
            content = @Content(schema = @Schema(implementation = UserApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PostMapping
    public ResponseEntity<ApiResponseData<UserApiResponse>> createUser(
            @Valid @RequestBody CreateUserApiRequest request) {
        UserResponse response = createUserUseCase.execute(request.toApplicationDto());
        UserApiResponse apiResponse = UserApiResponse.from(response);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponseData.success("User created successfully", apiResponse));
    }

    @Operation(summary = "Get user by ID", description = "Retrieves a user by their unique identifier")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User found",
            content = @Content(schema = @Schema(implementation = UserApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid user ID format"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseData<UserApiResponse>> getUserById(
            @Parameter(description = "User ID (UUID format)", required = true) @PathVariable String id) {
        UserResponse response = getUserUseCase.getById(id);
        UserApiResponse apiResponse = UserApiResponse.from(response);
        return ResponseEntity.ok(ApiResponseData.success(apiResponse));
    }

    @Operation(summary = "Get all users", description = "Retrieves a list of all users, optionally filtered by active status")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "List of users retrieved successfully")
    })
    @GetMapping
    public ResponseEntity<ApiResponseData<List<UserApiResponse>>> getAllUsers(
            @Parameter(description = "Filter by active status (true/false)")
            @RequestParam(value = "active", required = false) Boolean active) {
        List<UserResponse> responses = active != null && active
            ? getUserUseCase.getAllActiveUsers()
            : getUserUseCase.getAllUsers();

        List<UserApiResponse> apiResponses = responses.stream()
            .map(UserApiResponse::from)
            .toList();

        return ResponseEntity.ok(ApiResponseData.success(apiResponses));
    }

    @Operation(summary = "Update user", description = "Updates an existing user's information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User updated successfully",
            content = @Content(schema = @Schema(implementation = UserApiResponse.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input or validation error"),
        @ApiResponse(responseCode = "404", description = "User not found"),
        @ApiResponse(responseCode = "409", description = "Email already exists")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseData<UserApiResponse>> updateUser(
            @Parameter(description = "User ID (UUID format)", required = true) @PathVariable String id,
            @Valid @RequestBody UpdateUserApiRequest request) {
        UserResponse response = updateUserUseCase.execute(id, request.toApplicationDto());
        UserApiResponse apiResponse = UserApiResponse.from(response);
        return ResponseEntity.ok(ApiResponseData.success("User updated successfully", apiResponse));
    }

    @Operation(summary = "Soft delete user", description = "Deactivates a user without permanently removing their data")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User deactivated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID format"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseData<Void>> deleteUser(
            @Parameter(description = "User ID (UUID format)", required = true) @PathVariable String id) {
        deleteUserUseCase.softDelete(id);
        return ResponseEntity.ok(ApiResponseData.success("User deleted successfully", null));
    }

    @Operation(summary = "Hard delete user", description = "Permanently removes a user from the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User permanently deleted"),
        @ApiResponse(responseCode = "400", description = "Invalid user ID format"),
        @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponseData<Void>> permanentlyDeleteUser(
            @Parameter(description = "User ID (UUID format)", required = true) @PathVariable String id) {
        deleteUserUseCase.hardDelete(id);
        return ResponseEntity.ok(ApiResponseData.success("User permanently deleted", null));
    }
}
