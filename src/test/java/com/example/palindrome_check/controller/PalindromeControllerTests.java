package com.example.palindrome_check.controller;

import com.example.palindrome_check.model.PalindromeRequest;
import com.example.palindrome_check.service.PalindromeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PalindromeControllerTests {

    private MockMvc mockMvc;
    private PalindromeService service;
    private PalindromeController controller;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        service = mock(PalindromeService.class);
        controller = new PalindromeController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    void testCheckEndpoint_ValidPalindrome_CacheMiss() throws Exception {
        PalindromeRequest request = new PalindromeRequest("Seamus", "racecar");

        when(service.isCached("racecar")).thenReturn(false);
        when(service.checkAndPersist("Seamus", "racecar")).thenReturn(true);

        mockMvc.perform(post("/api/palindrome/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.palindrome").value(true))
                .andExpect(jsonPath("$.cached").value(false));

        verify(service, times(1)).isCached("racecar");
        verify(service, times(1)).checkAndPersist("Seamus", "racecar");
    }

    @Test
    void testCheckEndpoint_ValidPalindrome_CacheHit() throws Exception {
        PalindromeRequest request = new PalindromeRequest("Seamus", "level");

        when(service.isCached("level")).thenReturn(true);
        when(service.checkAndPersist("Seamus", "level")).thenReturn(true);

        mockMvc.perform(post("/api/palindrome/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.palindrome").value(true))
                .andExpect(jsonPath("$.cached").value(true));

        verify(service).isCached("level");
        verify(service).checkAndPersist("Seamus", "level");
    }

    @Test
    void testCheckEndpoint_InvalidInput_NullEmptyOrNonLetter() throws Exception {
        PalindromeRequest nullText = new PalindromeRequest("Seamus", null);
        PalindromeRequest emptyText = new PalindromeRequest("Seamus", "");
        PalindromeRequest invalidText = new PalindromeRequest("Seamus", "1234");

        for (PalindromeRequest r : List.of(nullText, emptyText, invalidText)) {
            mockMvc.perform(post("/api/palindrome/check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(r)))
                    .andExpect(status().isBadRequest());
        }

        verify(service, never()).checkAndPersist(anyString(), anyString());
    }

    @Test
    void testGetHistoryEndpoint() throws Exception {
        mockMvc.perform(get("/api/palindrome/history"))
                .andExpect(status().isOk());

        verify(service).getHistory();
    }

    @Test
    void testTestEndpoint() throws Exception {
        // Example palindrome and non-palindrome
        String palindrome = "madam";
        String nonPalindrome = "hello";

        mockMvc.perform(get("/api/palindrome/test")
                        .param("text", palindrome))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(PalindromeService.computePalindrome(palindrome))));

        mockMvc.perform(get("/api/palindrome/test")
                        .param("text", nonPalindrome))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(PalindromeService.computePalindrome(nonPalindrome))));
    }
}
