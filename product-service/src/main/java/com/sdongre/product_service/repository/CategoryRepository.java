package com.sdongre.product_service.repository;


import com.sdongre.product_service.model.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Page<Category> findAll(Pageable pageable);


    Page<Category> findByCategoryTitleContaining(String categoryTitle, Pageable pageable);

}