package com.ktmt.demoapplication.user.domain.service;

import com.ktmt.demoapplication.user.domain.model.Email;
import com.ktmt.demoapplication.user.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

/**
 * Domain Service for complex business logic that doesn't fit in a single entity
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Handles domain-level validations and business rules
 * - Dependency Inversion Principle (DIP): Depends on repository abstraction, not implementation
 */
@Service
public class UserDomainService {

    private final UserRepository userRepository;

    public UserDomainService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Domain rule: Email must be unique across all users
     */
    public void ensureEmailIsUnique(Email email) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists: " + email.getValue());
        }
    }

    /**
     * Domain rule: Email must be unique except for the current user
     */
    public void ensureEmailIsUniqueForUpdate(Email email, Email currentEmail) {
        if (!email.equals(currentEmail) && userRepository.existsByEmail(email)) {
            throw new IllegalStateException("Email already exists: " + email.getValue());
        }
    }
}
