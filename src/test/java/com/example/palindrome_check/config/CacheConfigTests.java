package com.example.palindrome_check.config;

import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

class CacheConfigTests {

    @Test
    void testPalindromeCacheBeanExists() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CacheConfig.class)) {
            Cache<String, Boolean> cache = context.getBean("palindromeCache", Cache.class);

            assertNotNull(cache, "Cache bean should not be null");

            assertNull(cache.getIfPresent("test"));
            cache.put("test", true);
            assertTrue(cache.getIfPresent("test"));
        }
    }


    void testCacheEvictionWorks() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(CacheConfig.class)) {
            Cache<String, Boolean> cache = context.getBean("palindromeCache", Cache.class);
            for (int i = 0; i < 10_001; i++) {
                cache.put("key" + i, true);}
            assertTrue(cache.estimatedSize() <= 10_000, "Cache should respect maximum size");
        }
    }
}
