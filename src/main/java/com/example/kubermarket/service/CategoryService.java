package com.example.kubermarket.service;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.domain.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> getCategories(){
        List<Category> categories= (List<Category>) categoryRepository.findAll();
        return categories;
    }


    public Category addCategory(String name) {
        Category category= Category.builder().name(name).build();
        categoryRepository.save(category);
        return  category;
    }

    public Category updateCategory(Long id,String name) {
        Category category = categoryRepository.findById(id).orElse(null);
        category.updateInfomation(name);
        categoryRepository.save(category);
        return  category;
    }

    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}
