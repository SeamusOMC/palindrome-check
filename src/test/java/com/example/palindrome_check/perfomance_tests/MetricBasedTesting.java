package com.example.palindrome_check.perfomance_tests;

import com.example.palindrome_check.repository.FilePalindromeRepository;
import com.example.palindrome_check.service.PalindromeService;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class MetricBasedTesting {
    //testing with and without caching,
    private PalindromeService service;
    private Cache<String, Boolean> cache;
    private FilePalindromeRepository repository;

    @BeforeEach
    public void setUp() {
        cache = Caffeine.newBuilder()
                .maximumSize(10_000)
                .expireAfterAccess(Duration.ofHours(1))
                .build();
        repository = new FilePalindromeRepository(); // uses data file
        service = new PalindromeService(cache, repository);
    }

    @Test
    public void testCachingImprovesPerformance() {
        String text = "TestOneTestTwoTestThreeerhttsetowttsetenotset"; // longer palindrome
        String username = "testUser";

        long startMiss = System.nanoTime();
        boolean firstResult = service.checkAndPersist(username, text);
        long durationMiss = System.nanoTime() - startMiss;

        long startHit = System.nanoTime();
        boolean secondResult = service.checkAndPersist(username, text);
        long durationHit = System.nanoTime() - startHit;

        assertTrue(firstResult, "First computation must be correct");
        assertTrue(secondResult, "Cached computation must be correct");

        System.out.printf("Cache miss: %d ns, Cache hit: %d ns%n", durationMiss, durationHit);
        System.out.printf("Cache miss: %d ms, Cache hit: %d ms%n", durationMiss / 1_000_000, durationHit / 1_000_000);
        assertTrue(durationHit < durationMiss, "Cached call should be faster than first call");
    }
}
