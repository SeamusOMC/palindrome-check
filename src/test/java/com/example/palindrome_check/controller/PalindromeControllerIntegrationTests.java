package com.example.palindrome_check.controller;

import com.example.palindrome_check.repository.FilePalindromeRepository;
import com.example.palindrome_check.service.PalindromeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PalindromeControllerIntegrationTests {
//region set up things
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FilePalindromeRepository repository;

    @Autowired
    private PalindromeService service;

    @BeforeEach
    void setup() {
        repository.clear();
        service.clearCache();
    }
//endregion
    @Test
    void testCheckEndpoint_PersistenceAndCache() throws Exception {
        String text = "level";
        String username = "Tester";

        mockMvc.perform(post("/api/palindrome/check").contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"text\":\"" + text + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.palindrome").value(true))
                .andExpect(jsonPath("$.cached").value(false));

        mockMvc.perform(post("/api/palindrome/check")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"" + username + "\",\"text\":\"" + text + "\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.palindrome").value(true))
                .andExpect(jsonPath("$.cached").value(true));
    }

    @Test
    void testCheckEndpoint_InvalidInput() throws Exception {
        String username = "Tester";
        String[] invalidInputs = {"1234", "racecar1", "hello!"};

        for (String text : invalidInputs) {
            mockMvc.perform(post("/api/palindrome/check")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content("{\"username\":\"" + username + "\",\"text\":\"" + text + "\"}"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.error").value("Input must only contain letters, while not being null."));
        }
    }
}