package com.sdongre.product_service.helper;

import com.sdongre.product_service.model.dto.CategoryDto;
import com.sdongre.product_service.model.entity.Category;

import java.util.Optional;

public interface CategoryMappingHelper {

    static CategoryDto map(final Category category) {
        final var parentCategory = Optional.ofNullable(category.getParentCategory())
                .orElseGet(Category::new);
        return CategoryDto.builder()
                .categoryId(category.getCategoryId())
                .categoryTitle(category.getCategoryTitle())
                .imageUrl(category.getImageUrl())
                .parentCategoryDto(
                        CategoryDto.builder()
                                .categoryId(parentCategory.getCategoryId())
                                .categoryTitle(parentCategory.getCategoryTitle())
                                .imageUrl(parentCategory.getImageUrl())
                                .build()
                )
                .build();
    }

    static Category map(CategoryDto categoryDto) {
        final var parentCategoryDto = Optional.ofNullable(categoryDto.getParentCategoryDto())
                .orElseGet(CategoryDto::new);
        return Category.builder()
                .categoryId(categoryDto.getCategoryId())
                .categoryTitle(categoryDto.getCategoryTitle())
                .imageUrl(categoryDto.getImageUrl())
                .parentCategory(Category.builder()
                        .categoryId(parentCategoryDto.getCategoryId())
                        .categoryTitle(parentCategoryDto.getCategoryTitle())
                        .imageUrl(parentCategoryDto.getImageUrl())
                        .build())
                .build();
    }

}
