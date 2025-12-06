package com.ktmt.demoapplication.content.infrastructure.persistence.repository;

import com.ktmt.demoapplication.content.domain.model.Content;
import com.ktmt.demoapplication.content.domain.model.ContentId;
import com.ktmt.demoapplication.content.domain.repository.IContentRepository;
import com.ktmt.demoapplication.content.infrastructure.persistence.mapper.ContentMapper;
import com.ktmt.demoapplication.content.infrastructure.persistence.entity.ContentJpaEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class ContentRepositoryImpl implements IContentRepository {

    private final ContentJpaRepository jpa;

    public ContentRepositoryImpl(ContentJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Content> getItem(ContentId id) {
        if (id == null) return Optional.empty();
        return jpa.findById(id.getValue()).map(ContentMapper::toDomain);
    }

    @Override
    public List<Content> getAllItems() {
        return jpa.findAll().stream().map(ContentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<Content> searchItems(String criteria) {
        if (criteria == null || criteria.trim().isEmpty()) {
            return getAllItems();
        }
        List<ContentJpaEntity> found = jpa.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(criteria, criteria);
        return found.stream().map(ContentMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Content addItem(Content content) {
        ContentJpaEntity saved = jpa.save(ContentMapper.toEntity(content));
        return ContentMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void updateItem(Content content) {
        // Use save as upsert. Ensure business validations done before calling.
        jpa.save(ContentMapper.toEntity(content));
    }

    @Override
    @Transactional
    public void deleteItem(ContentId id) {
        if (id == null) return;
        jpa.deleteById(id.getValue());
    }
}
