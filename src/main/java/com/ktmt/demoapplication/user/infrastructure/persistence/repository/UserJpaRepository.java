package com.ktmt.demoapplication.user.infrastructure.persistence.repository;

import com.ktmt.demoapplication.user.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Spring Data JPA Repository
 *
 * SOLID Principles Applied:
 * - Interface Segregation Principle (ISP): Extends only necessary JPA interfaces
 * - Dependency Inversion Principle (DIP): Framework provides implementation
 */
@Repository
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {

    Optional<UserJpaEntity> findByEmail(String email);

    @Query("SELECT u FROM UserJpaEntity u WHERE u.active = true")
    List<UserJpaEntity> findAllActive();

    boolean existsByEmail(String email);
}
