package com.ktmt.demoapplication.user.application.usecase;

import com.ktmt.demoapplication.user.application.dto.UserResponse;
import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.model.UserId;
import com.ktmt.demoapplication.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Use Case for retrieving users
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Handles only user retrieval logic
 * - Dependency Inversion Principle (DIP): Depends on UserRepository abstraction
 */
@Service
@Transactional(readOnly = true)
public class GetUserUseCase {

    private final UserRepository userRepository;

    public GetUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getById(String id) {
        UserId userId = UserId.from(id);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
        return UserResponse.from(user);
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
            .map(UserResponse::from)
            .toList();
    }

    public List<UserResponse> getAllActiveUsers() {
        return userRepository.findAllActive().stream()
            .map(UserResponse::from)
            .toList();
    }
}
