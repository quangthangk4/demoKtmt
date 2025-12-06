package com.ktmt.demoapplication.content.infrastructure.persistence.repository;

import com.ktmt.demoapplication.content.infrastructure.persistence.entity.ContentJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContentJpaRepository extends JpaRepository<ContentJpaEntity, UUID> {

    // simple search by title or description
    List<ContentJpaEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
