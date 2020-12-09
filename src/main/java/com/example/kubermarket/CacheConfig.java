package com.example.kubermarket;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.jcache.config.JCacheConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig extends JCacheConfigurerSupport {

    @Autowired
    RedisConnectionFactory redisConnectionFactory;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    RedisConnectionFactory connectionFactory;

    @Bean
    public RedisCacheManager CacheManager() {
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .entryTtl(Duration.ofHours(1L))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));

        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory).cacheDefaults(redisCacheConfiguration).build();
        return redisCacheManager;
    }
}
