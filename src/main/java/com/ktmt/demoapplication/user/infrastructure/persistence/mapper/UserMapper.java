package com.ktmt.demoapplication.user.infrastructure.persistence.mapper;

import com.ktmt.demoapplication.user.domain.model.Email;
import com.ktmt.demoapplication.user.domain.model.User;
import com.ktmt.demoapplication.user.domain.model.UserId;
import com.ktmt.demoapplication.user.infrastructure.persistence.entity.UserJpaEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper between Domain Model and JPA Entity
 *
 * SOLID Principles Applied:
 * - Single Responsibility Principle (SRP): Only responsible for mapping between domain and persistence
 * - Open/Closed Principle (OCP): Can be extended for additional mapping logic
 * - Dependency Inversion Principle (DIP): Infrastructure depends on domain model, not vice versa
 */
@Component
public class UserMapper {

    /**
     * Convert domain User to JPA entity
     */
    public UserJpaEntity toJpaEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.getId().getValue());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setEmail(user.getEmail().getValue());
        entity.setAge(user.getAge());
        entity.setActive(user.isActive());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setUpdatedAt(user.getUpdatedAt());
        return entity;
    }

    /**
     * Convert JPA entity to domain User
     */
    public User toDomainModel(UserJpaEntity entity) {
        return User.reconstitute(
            UserId.from(entity.getId()),
            entity.getFirstName(),
            entity.getLastName(),
            Email.from(entity.getEmail()),
            entity.getAge(),
            entity.isActive(),
            entity.getCreatedAt(),
            entity.getUpdatedAt()
        );
    }
}
