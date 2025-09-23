package com.example.palindrome_check.startup;

import com.example.palindrome_check.model.PalindromeRecord;
import com.example.palindrome_check.repository.PalindromeRepository;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class StartupCacheLoaderTests {

    private PalindromeRepository mockRepo;
    private Cache<String, Boolean> mockCache;
    private StartupCacheLoader loader;

    @BeforeEach
    void setUp() {
        mockRepo = mock(PalindromeRepository.class);
        mockCache = mock(Cache.class);

        loader = new StartupCacheLoader(mockRepo, mockCache);
    }

    @Test
    void run_populatesCacheWithExistingRecords() throws Exception {
        PalindromeRecord r1 = new PalindromeRecord("user1", "madam", true, Instant.now());
        PalindromeRecord r2 = new PalindromeRecord("user2", "racecar", true, Instant.now());

        when(mockRepo.findAll()).thenReturn(List.of(r1, r2));

        loader.run(mock(ApplicationArguments.class));

        verify(mockCache).put("madam", true);
        verify(mockCache).put("racecar", true);
    }

    @Test
    void run_withEmptyRepository_doesNothing() throws Exception {
        when(mockRepo.findAll()).thenReturn(List.of());

        loader.run(mock(ApplicationArguments.class));

        verify(mockCache, never()).put(anyString(), anyBoolean());
    }


    void run_handlesNullRecordsGracefully() throws Exception {
        List<PalindromeRecord> recordsWithNull = new ArrayList<>();
        recordsWithNull.add(null);

        when(mockRepo.findAll()).thenReturn(recordsWithNull);

        assertDoesNotThrow(() -> loader.run(mock(ApplicationArguments.class)));

        verify(mockCache, never()).put(anyString(), anyBoolean());
    }

}
