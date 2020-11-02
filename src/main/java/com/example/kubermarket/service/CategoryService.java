package com.example.kubermarket.service;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.domain.CategoryRepository;
import com.example.kubermarket.domain.Product;
import com.example.kubermarket.dto.CategoryDto;
import com.example.kubermarket.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CategoryRepository categoryRepository;

    //@Cacheable(key = "CategoryList" , value = "Category",cacheManager = "CacheManager")
    public List<CategoryDto> getCategories(){
        ValueOperations<String,Object> redisData = redisTemplate.opsForValue();
        String Key= "GetCategoryList";
        List<Category> categories= (List<Category>) categoryRepository.findAll();
        List<CategoryDto> categoryDtoList=new ArrayList<>();
        for(Category category:categories){
            categoryDtoList.add(this.convertEntityToDto(category));
        }
        redisData.set(Key,categoryDtoList);
        return categoryDtoList;
    }

    public Category addCategory(String name) {
        Category category= Category.builder().name(name).build();
        categoryRepository.save(category);
        String Key= "GetCategoryList";
        redisTemplate.delete(Key);
        return  category;
    }

    //@CacheEvict(key = "CategoryList",value = "Category",cacheManager = "CacheManager")
    public Category updateCategory(Long id,String name) {
        Category category = categoryRepository.findById(id).orElse(null);
        category.setName(name);
        categoryRepository.save(category);
        String Key= "GetCategoryList";
        redisTemplate.delete(Key);
        return  category;
    }

    //@CacheEvict(key = "CategoryList",value = "Category",cacheManager = "CacheManager")
    public void deleteCategory(Long id) {

        categoryRepository.deleteById(id);
        String Key= "GetCategoryList";
        redisTemplate.delete(Key);
    }

    private CategoryDto convertEntityToDto(Category category) {
        CategoryDto categoryDto= new CategoryDto();
        return categoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

}
