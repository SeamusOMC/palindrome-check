package com.example.palindrome_check.service;

import com.example.palindrome_check.model.PalindromeRecord;
import com.example.palindrome_check.repository.PalindromeRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PalindromeServiceTests {

    private PalindromeService service;
    private Cache<String, Boolean> cache;
    private PalindromeRepository repository;

    @BeforeEach
    void setUp() {
        cache = Caffeine.newBuilder().maximumSize(10_000).expireAfterAccess(Duration.ofHours(1)).build();

        repository = new PalindromeRepository() {
            private final List<PalindromeRecord> records = new ArrayList<>();

            @Override
            public void save(PalindromeRecord record) {
                records.add(record);
            }

            @Override
            public List<PalindromeRecord> findAll() {
                return new ArrayList<>(records);
            }
        };

        service = new PalindromeService(cache, repository);
    }

    @Test
    void testComputePalindromeBranches() {
        // Normal palindrome
        assertTrue(PalindromeService.computePalindrome("racecar"));

        // Single character
        assertTrue(PalindromeService.computePalindrome("a"));

        // Non-palindrome
        assertFalse(PalindromeService.computePalindrome("abc"));

        // null input
        assertFalse(PalindromeService.computePalindrome(null));
    }

    @Test
    void testCachingMechanism() {
        String text = "level";
        String username = "testUser";

        boolean firstResult = service.checkAndPersist(username, text);
        assertTrue(firstResult);
        assertTrue(service.isCached(text));

        boolean secondResult = service.checkAndPersist(username, text);
        assertTrue(secondResult);

        assertEquals(firstResult, secondResult);
    }

    @Test
    void testIsCached() {
        String text = "level";
        String username = "testUser";

        assertFalse(service.isCached(text));

        service.checkAndPersist(username, text);

        assertTrue(service.isCached(text));
    }

    @Test
    void testHistory() {
        service.checkAndPersist("testUser", "racecar");
        service.checkAndPersist("testUser2", "hello");

        var history = service.getHistory();
        assertEquals(2, history.size());

        assertTrue(history.get(0).isPalindrome()); // racecar
        assertFalse(history.get(1).isPalindrome()); // hello
    }
}
