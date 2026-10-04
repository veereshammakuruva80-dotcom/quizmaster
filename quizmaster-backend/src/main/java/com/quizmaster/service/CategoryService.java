package com.quizmaster.service;

import com.quizmaster.entity.Category;
import com.quizmaster.payload.request.CategoryRequest;
import com.quizmaster.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> getAllActiveCategories() {
        return categoryRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
    }

    @Transactional
    public Category createCategory(CategoryRequest request) {
        String slug = request.getSlug();
        if (slug == null || slug.isBlank()) {
            slug = request.getName().toLowerCase().replaceAll("[^a-z0-9]+", "-").replaceAll("^-|-$", "");
        }

        Category category = new Category();
        category.setName(request.getName());
        category.setSlug(slug);
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon() != null ? request.getIcon() : "bi-bookmark");
        category.setColorCode(request.getColorCode() != null ? request.getColorCode() : "#4f46e5");
        category.setActive(request.getActive() != null ? request.getActive() : true);

        return categoryRepository.save(category);
    }

    @Transactional
    public Category updateCategory(Long id, CategoryRequest request) {
        Category category = getCategoryById(id);
        category.setName(request.getName());
        if (request.getSlug() != null && !request.getSlug().isBlank()) {
            category.setSlug(request.getSlug());
        }
        category.setDescription(request.getDescription());
        category.setIcon(request.getIcon());
        category.setColorCode(request.getColorCode());
        if (request.getActive() != null) {
            category.setActive(request.getActive());
        }

        return categoryRepository.save(category);
    }

    @Transactional
    public void deleteCategory(Long id) {
        Category category = getCategoryById(id);
        categoryRepository.delete(category);
    }
}
