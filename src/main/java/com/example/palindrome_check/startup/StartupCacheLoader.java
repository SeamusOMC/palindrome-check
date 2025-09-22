package com.example.palindrome_check.startup;

import com.example.palindrome_check.repository.PalindromeRepository;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class StartupCacheLoader implements ApplicationRunner {

    private final PalindromeRepository repo;
    private final Cache<String, Boolean> cache;

    public StartupCacheLoader(PalindromeRepository repo, Cache<String, Boolean> cache) {
        this.repo = repo;
        this.cache = cache;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        repo.findAll().forEach(rec -> cache.put(rec.getText().toLowerCase(), rec.isPalindrome()));
    }
}
