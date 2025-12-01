package com.ktmt.demoapplication.user.infrastructure.persistence.repository;

import com.ktmt.demoapplication.user.domain.model.Email;
import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.model.UserId;
import com.ktmt.demoapplication.user.domain.repository.UserRepository;
import com.ktmt.demoapplication.user.infrastructure.persistence.entity.UserJpaEntity;
import com.ktmt.demoapplication.user.infrastructure.persistence.mapper.UserMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of UserRepository (Adapter in Hexagonal Architecture)
 *
 * SOLID Principles Applied:
 * - Dependency Inversion Principle (DIP): Implements domain interface, depends on abstractions
 * - Single Responsibility Principle (SRP): Only responsible for persistence operations
 * - Liskov Substitution Principle (LSP): Can substitute the interface without breaking behavior
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository jpaRepository;
    private final UserMapper mapper;

    public UserRepositoryImpl(UserJpaRepository jpaRepository, UserMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = mapper.toJpaEntity(user);
        UserJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomainModel(savedEntity);
    }

    @Override
    public Optional<User> findById(UserId id) {
        return jpaRepository.findById(id.getValue())
            .map(mapper::toDomainModel);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.getValue())
            .map(mapper::toDomainModel);
    }

    @Override
    public List<User> findAll() {
        return jpaRepository.findAll().stream()
            .map(mapper::toDomainModel)
            .toList();
    }

    @Override
    public List<User> findAllActive() {
        return jpaRepository.findAllActive().stream()
            .map(mapper::toDomainModel)
            .toList();
    }

    @Override
    public void deleteById(UserId id) {
        jpaRepository.deleteById(id.getValue());
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.getValue());
    }
}
