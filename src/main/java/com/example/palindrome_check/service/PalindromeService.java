package com.example.palindrome_check.service;

import com.example.palindrome_check.model.PalindromeRecord;
import com.example.palindrome_check.repository.PalindromeRepository;
import com.github.benmanes.caffeine.cache.Cache;
import com.example.palindrome_check.model.PalindromeResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PalindromeService {

    private final Cache<String, Boolean> cache;
    private final PalindromeRepository repository;

    public PalindromeService(Cache<String, Boolean> cache, PalindromeRepository repository) {
        this.cache = cache;
        this.repository = repository;
    }

    public boolean isCached(String text) {
        String key = canonical(text);
        return cache.getIfPresent(key) != null;
    }

    public boolean checkAndPersist(String username, String text) {
        validateInput(text);
        String key = canonical(text);
        Boolean cached = cache.getIfPresent(key);
        if (cached != null) return cached;

        boolean isPal = computePalindrome(text);
        PalindromeRecord rec = new PalindromeRecord(username, text, isPal, Instant.now());
        repository.save(rec);

        cache.put(key, isPal);
        return isPal;
    }

    public static boolean computePalindrome(String s) {
        if (s == null) return false;
        s = s.toLowerCase();
        return isPalindromeBase(0, s.length() - 1, s);
    }

    private static boolean isPalindromeBase(int i, int j, String s) {
        if (i >= j) return true;
        if (s.charAt(i) != s.charAt(j)) return false;
        return isPalindromeBase(i + 1, j - 1, s);
    }

    private String canonical(String s) {
        return s.toLowerCase();
    }

    private void validateInput(String text) {
        if (text == null || text.isEmpty() || !text.matches("^[A-Za-z]+$")) {
            throw new IllegalArgumentException("Input must only contain letters.");
        }
    }

    public void clearCache() {
        cache.invalidateAll();
    }

    public List<PalindromeResponse> getHistory() {
        return repository.findAll().stream()
                .map(r -> new PalindromeResponse(
                        r.getUsername(),
                        r.getText(),
                        r.isPalindrome(),
                        false, // history doesn’t care about cache
                        r.getTimestamp()
                ))
                .collect(Collectors.toList());
    }
}