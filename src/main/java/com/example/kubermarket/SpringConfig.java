package com.example.kubermarket;

import com.example.kubermarket.domain.CategoryRepositoryJpa;
import com.example.kubermarket.domain.CategoryRepositoryImpl;
import com.example.kubermarket.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

/**
 * Category는 Spring Data JPA 말고 그냥 JPA를 사용
 */
@Configuration
public class SpringConfig {

    private final CategoryRepositoryJpa categoryRepositoryjpa; //구현체를 넣어줘야한다.
    private final RedisConfig redisConfig;
    //Jpa를 사용할때는 EntityManager가 필수이다.
    private EntityManager em;

    @Autowired
    public SpringConfig(CategoryRepositoryJpa categoryRepositoryjpa,
                        RedisConfig redisConfig,
                        EntityManager em) {
        this.categoryRepositoryjpa = categoryRepositoryjpa;
        this.redisConfig = redisConfig;
        this.em=em;
    }

    @Bean
    public CategoryService categoryService(){
        CategoryService categoryService = new CategoryService(redisConfig.redisTemplate(redisConfig.redisConnectionFactory()),categoryRepositoryjpa);
        return categoryService;
    }

    @Bean
    public CategoryRepositoryJpa categoryRepositoryjpa() {  //-> Spring data Jpa는 Bean 설정은 안해도된다.
        return new CategoryRepositoryImpl(em);
    }

//    @Bean
//    public CacheService cacheService(){
//        CacheService cacheService=new CacheService();
//        return cacheService;
//    }
}
