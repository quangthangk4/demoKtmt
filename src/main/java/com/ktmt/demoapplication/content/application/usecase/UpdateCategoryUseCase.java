package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.application.dto.CategoryResponse;
import com.ktmt.demoapplication.content.application.dto.UpdateCategoryRequest;
import com.ktmt.demoapplication.content.domain.model.Category;
import com.ktmt.demoapplication.content.domain.repository.ICategoryRepository;


import java.util.UUID;

public class UpdateCategoryUseCase {
    private final ICategoryRepository iCategoryRepository;

    public UpdateCategoryUseCase(ICategoryRepository iCategoryRepository){
        this.iCategoryRepository = iCategoryRepository;
    }

    public void execute(UUID categoryId, UpdateCategoryRequest req){
        Category existingCategory = iCategoryRepository.getCategory(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with id: " + categoryId));

        existingCategory.updateInformation(
                req.name(),
                req.description()
        );

        iCategoryRepository.updateCategory(existingCategory);
    }
}
