package com.sboot.service;

import com.sboot.entity.Category;
import com.sboot.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public CategoryService(CategoryRepository categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    public List<Category> getAllCategories() {
        return categoryRepo.findAll();
    }

    public Category getCategory(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public Category createCategory(Category category) {
        return categoryRepo.save(category);
    }

    public Category updateCategory(Long id, Category category) {
        Category existing = categoryRepo.findById(id).orElse(null);
        if (existing == null) return null;

        existing.setCategoryName(category.getCategoryName());
        return categoryRepo.save(existing);
    }

    public void deleteCategory(Long id) {
        categoryRepo.deleteById(id);
    }
}
