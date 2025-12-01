package com.ktmt.demoapplication.user.domain.model;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Aggregate Root (DDD Pattern)
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Manages user business logic only
 * - Open/Closed Principle (OCP): Open for extension through inheritance, closed for modification
 * - Encapsulation: Business rules are encapsulated within domain model
 */
public class User {
    private UserId id;
    private String firstName;
    private String lastName;
    private Email email;
    private Integer age;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Private constructor to enforce creation through factory methods
    private User() {
    }

    /**
     * Factory method to create new User
     */
    public static User create(String firstName, String lastName, String email, Integer age) {
        User user = new User();
        user.id = UserId.create();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(Email.from(email));
        user.setAge(age);
        user.active = true;
        user.createdAt = LocalDateTime.now();
        user.updatedAt = LocalDateTime.now();
        return user;
    }

    /**
     * Factory method to reconstitute User from persistence
     */
    public static User reconstitute(UserId id, String firstName, String lastName,
                                   Email email, Integer age, boolean active,
                                   LocalDateTime createdAt, LocalDateTime updatedAt) {
        User user = new User();
        user.id = id;
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.age = age;
        user.active = active;
        user.createdAt = createdAt;
        user.updatedAt = updatedAt;
        return user;
    }

    /**
     * Business method to update user information
     */
    public void updateInformation(String firstName, String lastName, String email, Integer age) {
        setFirstName(firstName);
        setLastName(lastName);
        setEmail(Email.from(email));
        setAge(age);
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business method to deactivate user
     */
    public void deactivate() {
        this.active = false;
        this.updatedAt = LocalDateTime.now();
    }

    /**
     * Business method to activate user
     */
    public void activate() {
        this.active = true;
        this.updatedAt = LocalDateTime.now();
    }

    // Setters with business validation
    private void setFirstName(String firstName) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be null or empty");
        }
        if (firstName.length() > 50) {
            throw new IllegalArgumentException("First name cannot exceed 50 characters");
        }
        this.firstName = firstName.trim();
    }

    private void setLastName(String lastName) {
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be null or empty");
        }
        if (lastName.length() > 50) {
            throw new IllegalArgumentException("Last name cannot exceed 50 characters");
        }
        this.lastName = lastName.trim();
    }

    private void setEmail(Email email) {
        if (email == null) {
            throw new IllegalArgumentException("Email cannot be null");
        }
        this.email = email;
    }

    private void setAge(Integer age) {
        if (age == null) {
            throw new IllegalArgumentException("Age cannot be null");
        }
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        this.age = age;
    }

    // Getters
    public UserId getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Email getEmail() {
        return email;
    }

    public Integer getAge() {
        return age;
    }

    public boolean isActive() {
        return active;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email=" + email +
                ", age=" + age +
                ", active=" + active +
                '}';
    }
}
