package com.example.kubermarket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CacheService {
    @Caching(evict = {
            @CacheEvict(value = "AddressProduct", key = "#address"),
            @CacheEvict(value = "CategoryProduct", key = "#categoryName")
    })
    public boolean deleteProductCache(String address, String categoryName) {
        log.debug("deleteBoardCache - address {}, categoryName {}", address, categoryName);
        return true;
    }
}

