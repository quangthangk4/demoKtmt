package com.ktmt.demoapplication.content.infrastructure.persistence.repository;

import com.ktmt.demoapplication.content.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryJpaEntity, UUID> {
    // You can add custom queries here later if needed
}
