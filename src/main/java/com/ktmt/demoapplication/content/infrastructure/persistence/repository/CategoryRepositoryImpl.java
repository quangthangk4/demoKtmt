package com.ktmt.demoapplication.content.infrastructure.persistence.repository;

import com.ktmt.demoapplication.content.domain.model.Category;
import com.ktmt.demoapplication.content.domain.repository.ICategoryRepository;
import com.ktmt.demoapplication.content.infrastructure.persistence.mapper.CategoryMapper;
import com.ktmt.demoapplication.content.infrastructure.persistence.entity.CategoryJpaEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class CategoryRepositoryImpl implements ICategoryRepository {

    private final CategoryJpaRepository jpa;

    public CategoryRepositoryImpl(CategoryJpaRepository jpa) {
        this.jpa = jpa;
    }

    @Override
    public Optional<Category> getCategory(UUID id) {
        return jpa.findById(id).map(CategoryMapper::toDomain);
    }

    @Override
    public List<Category> getCategories() {
        return jpa.findAll().stream().map(CategoryMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Category addCategory(Category category) {
        CategoryJpaEntity saved = jpa.save(CategoryMapper.toEntity(category));
        return CategoryMapper.toDomain(saved);
    }

    @Override
    @Transactional
    public void updateCategory(Category category) {
        // Save will act as upsert. Ensure the id exists per your application logic if needed.
        jpa.save(CategoryMapper.toEntity(category));
    }

    @Override
    @Transactional
    public void deleteCategory(UUID id) {
        jpa.deleteById(id);
    }
}
