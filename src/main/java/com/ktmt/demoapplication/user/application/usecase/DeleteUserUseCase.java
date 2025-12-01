package com.ktmt.demoapplication.user.application.usecase;

import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.model.UserId;
import com.ktmt.demoapplication.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Use Case for deleting a user (soft delete - deactivate)
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Handles only user deletion logic
 * - Dependency Inversion Principle (DIP): Depends on UserRepository abstraction
 */
@Service
@Transactional
public class DeleteUserUseCase {

    private final UserRepository userRepository;

    public DeleteUserUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Soft delete - deactivate user
     */
    public void softDelete(String id) {
        UserId userId = UserId.from(id);
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));

        user.deactivate();
        userRepository.save(user);
    }

    /**
     * Hard delete - permanently remove user
     */
    public void hardDelete(String id) {
        UserId userId = UserId.from(id);
        if (!userRepository.findById(userId).isPresent()) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        userRepository.deleteById(userId);
    }
}
