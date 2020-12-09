package com.example.kubermarket.service;

import com.example.kubermarket.domain.Category;
import com.example.kubermarket.domain.CategoryRepository;
import com.example.kubermarket.domain.CategoryRepositoryJpa;
import com.example.kubermarket.dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class CategoryService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CategoryRepositoryJpa categoryRepositoryjpa;

    @Autowired
    public CategoryService(RedisTemplate<String, Object> redisTemplate, CategoryRepositoryJpa categoryRepositoryjpa) {
        this.redisTemplate = redisTemplate;
        this.categoryRepositoryjpa = categoryRepositoryjpa;
    }

    //@Cacheable(key = "CategoryList" , value = "Category",cacheManager = "CacheManager")
    public List<CategoryDto> getCategories(){
        ValueOperations<String,Object> redisData = redisTemplate.opsForValue();
        String Key= "GetCategoryList";
        List<Category> categories;
        List<CategoryDto> categoryDtoList;
        if(redisData.get(Key)!=null){
            categories= (List<Category>) redisData.get(Key);
            categoryDtoList=new ArrayList<>();
            for(Category category:categories){
                categoryDtoList.add(this.convertEntityToDto(category));
            }
        }else {
            categories= (List<Category>) categoryRepositoryjpa.findAll();
            categoryDtoList=new ArrayList<>();
            for(Category category:categories){
                categoryDtoList.add(this.convertEntityToDto(category));
            }
            redisData.set(Key,categoryDtoList);
        }
        return categoryDtoList;
    }

    public Category addCategory(String name) {
        Category category= Category.builder().name(name).build();
        categoryRepositoryjpa.save(category);
        String Key= "GetCategoryList";
        redisTemplate.delete(Key);
        return  category;
    }

    //@CacheEvict(key = "CategoryList",value = "Category",cacheManager = "CacheManager")
    public Category updateCategory(Long id,String name) {
        Category category = categoryRepositoryjpa.findById(id).orElse(null);
        category.updateInformation(name);
        categoryRepositoryjpa.save(category);
        String Key= "GetCategoryList";
        redisTemplate.delete(Key);
        return  category;
    }

    //@CacheEvict(key = "CategoryList",value = "Category",cacheManager = "CacheManager")
    public void deleteCategory(Long id) {

        categoryRepositoryjpa.deleteById(id);
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
