package com.ktmt.demoapplication.user.domain.repository;

import com.ktmt.demoapplication.user.domain.model.Email;
import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.model.UserId;

import java.util.List;
import java.util.Optional;

/**
 * User Repository Interface (Port in Hexagonal Architecture)
 *
 * SOLID Principles Applied:
 * - Dependency Inversion Principle (DIP): High-level domain doesn't depend on low-level infrastructure
 * - Interface Segregation Principle (ISP): Focused interface with only necessary methods
 * - Single Responsibility Principle (SRP): Responsible only for user persistence abstraction
 */
public interface UserRepository {

    /**
     * Save a user (create or update)
     */
    User save(User user);

    /**
     * Find user by ID
     */
    Optional<User> findById(UserId id);

    /**
     * Find user by email
     */
    Optional<User> findByEmail(Email email);

    /**
     * Find all users
     */
    List<User> findAll();

    /**
     * Find all active users
     */
    List<User> findAllActive();

    /**
     * Delete user by ID
     */
    void deleteById(UserId id);

    /**
     * Check if user exists by email
     */
    boolean existsByEmail(Email email);
}
