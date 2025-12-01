package com.ktmt.demoapplication.user.application.usecase;

import com.ktmt.demoapplication.user.application.dto.UpdateUserRequest;
import com.ktmt.demoapplication.user.application.dto.UserResponse;
import com.ktmt.demoapplication.user.domain.model.Email;
import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.model.UserId;
import com.ktmt.demoapplication.user.domain.repository.UserRepository;
import com.ktmt.demoapplication.user.domain.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case for updating user information
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Handles only user update logic
 * - Dependency Inversion Principle (DIP): Depends on abstractions
 */
@Service
@Transactional
public class UpdateUserUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public UpdateUserUseCase(UserRepository userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }

    public UserResponse execute(String id, UpdateUserRequest request) {
        UserId userId = UserId.from(id);

        // Find existing user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        // Validate business rules
        Email newEmail = Email.from(request.email());
        userDomainService.ensureEmailIsUniqueForUpdate(newEmail, user.getEmail());

        // Update user
        user.updateInformation(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.age()
        );

        // Persist changes
        User updatedUser = userRepository.save(user);

        // Return response DTO
        return UserResponse.from(updatedUser);
    }
}
