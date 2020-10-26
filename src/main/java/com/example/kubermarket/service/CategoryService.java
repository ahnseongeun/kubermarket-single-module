package com.example.kubermarket.service;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.domain.CategoryRepository;
import com.example.kubermarket.domain.Product;
import com.example.kubermarket.dto.CategoryDto;
import com.example.kubermarket.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Cacheable(key = "#num",value = "Category",cacheManager = "CacheManager")
    public List<CategoryDto> getCategories(Integer num){
        List<Category> categories= (List<Category>) categoryRepository.findAll();
        List<CategoryDto> categoryDtoList=new ArrayList<>();
        for(Category category:categories){
            categoryDtoList.add(this.convertEntityToDto(category));
        }
        return categoryDtoList;
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

    private CategoryDto convertEntityToDto(Category category) {
        CategoryDto categoryDto= new CategoryDto();
        return categoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
