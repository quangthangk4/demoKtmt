package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.domain.model.Category;
import com.ktmt.demoapplication.content.domain.repository.ICategoryRepository;
import java.util.UUID;

public class DeleteCategoryUseCase {
    private final ICategoryRepository iCategoryRepository;

    public DeleteCategoryUseCase(ICategoryRepository iCategoryRepository){
        this.iCategoryRepository = iCategoryRepository;
    }

    public void execute(UUID categoryId){
        Category existingCategory = iCategoryRepository.getCategory(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));

        iCategoryRepository.deleteCategory(existingCategory.getId());
    }
}
