package com.example.kubermarket;

import com.example.kubermarket.domain.CategoryRepository;
import com.example.kubermarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Category는 Spring Data JPA 말고 그냥 JPA를 사용
 */
@Configuration
public class SpringConfig {

    private final CategoryRepository categoryRepository;
    private final RedisConfig redisConfig;

    @Autowired
    public SpringConfig(CategoryRepository categoryRepository, RedisConfig redisConfig) {
        this.categoryRepository = categoryRepository;
        this.redisConfig = redisConfig;
    }

    @Bean
    public CategoryService categoryService(){
        CategoryService categoryService = new CategoryService(redisConfig.redisTemplate(redisConfig.redisConnectionFactory()), categoryRepository);
        return categoryService;
    }
}
