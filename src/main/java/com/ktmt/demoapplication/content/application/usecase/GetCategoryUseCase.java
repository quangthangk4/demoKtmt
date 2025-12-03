package com.ktmt.demoapplication.content.application.usecase;

import com.ktmt.demoapplication.content.application.dto.CategoryResponse;
import com.ktmt.demoapplication.content.domain.model.Category;
import com.ktmt.demoapplication.content.domain.repository.ICategoryRepository;
import java.util.List;
import java.util.UUID;

public class GetCategoryUseCase {
    private final ICategoryRepository iCategoryRepository;

    public GetCategoryUseCase(ICategoryRepository iCategoryRepository){
        this.iCategoryRepository = iCategoryRepository;
    }

    public CategoryResponse getCategoryById(UUID categoryId){
        Category existingCategory = iCategoryRepository.getCategory(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found with id: " + categoryId));
        return CategoryResponse.from(existingCategory);
    }

    public List<CategoryResponse> getAllCategory(){
        List<Category> allCategories = iCategoryRepository.getCategories();

        return allCategories.stream()
                .map(CategoryResponse::from)
                .toList();
    }

}
