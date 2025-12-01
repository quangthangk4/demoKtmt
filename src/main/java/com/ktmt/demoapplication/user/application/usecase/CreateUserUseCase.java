package com.ktmt.demoapplication.user.application.usecase;

import com.ktmt.demoapplication.user.application.dto.CreateUserRequest;
import com.ktmt.demoapplication.user.application.dto.UserResponse;
import com.ktmt.demoapplication.user.domain.model.Email;
import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.repository.UserRepository;
import com.ktmt.demoapplication.user.domain.service.UserDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case for creating a new user
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Handles only user creation logic
 * - Dependency Inversion Principle (DIP): Depends on abstractions (UserRepository interface)
 * - Open/Closed Principle (OCP): Can be extended through inheritance without modifying existing code
 */
@Service
@Transactional
public class CreateUserUseCase {

    private final UserRepository userRepository;
    private final UserDomainService userDomainService;

    public CreateUserUseCase(UserRepository userRepository, UserDomainService userDomainService) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
    }

    public UserResponse execute(CreateUserRequest request) {
        // Validate business rules
        Email email = Email.from(request.email());
        userDomainService.ensureEmailIsUnique(email);

        // Create domain entity
        User user = User.create(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.age()
        );

        // Persist entity
        User savedUser = userRepository.save(user);

        // Return response DTO
        return UserResponse.from(savedUser);
    }
}
