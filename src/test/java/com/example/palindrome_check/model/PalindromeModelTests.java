package com.example.palindrome_check.model;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PalindromeModelTests {


    @Test
    void palindromeRecord_gettersSetters_work() {
        Instant now = Instant.now();
        PalindromeRecord record = new PalindromeRecord("testUser", "madam", true, now);

        assertEquals("testUser", record.getUsername());
        assertEquals("madam", record.getText());
        assertTrue(record.isPalindrome());
        assertEquals(now, record.getTimestamp());

        Instant newTime = now.plusSeconds(60);
        record.setUsername("testUser2");
        record.setText("civic");
        record.setPalindrome(false);
        record.setTimestamp(newTime);

        assertEquals("testUser2", record.getUsername());
        assertEquals("civic", record.getText());
        assertFalse(record.isPalindrome());
        assertEquals(newTime, record.getTimestamp());
    }


    @Test
    void palindromeRequest_gettersSetters_work() {
        PalindromeRequest request = new PalindromeRequest("testUser", "civic");

        assertEquals("testUser", request.getUsername());
        assertEquals("civic", request.getText());

        request.setUsername("testUser2");
        request.setText("level");

        assertEquals("testUser2", request.getUsername());
        assertEquals("level", request.getText());
    }


    @Test
    void palindromeResponse_getters_work() {
        Instant now = Instant.now();
        PalindromeResponse response = new PalindromeResponse("testUser", "rotor", true, false, now);

        assertEquals("testUser", response.getUsername());
        assertEquals("rotor", response.getText());
        assertTrue(response.isPalindrome());
        assertFalse(response.isCached());
        assertEquals(now, response.getTimestamp());
    }

    @Test
    void palindromeRecord_emptyConstructor_works() {
        PalindromeRecord record = new PalindromeRecord();
        assertNotNull(record);
        record.setUsername("testUser");
        record.setText("madam");
        record.setPalindrome(true);
        Instant now = Instant.now();
        record.setTimestamp(now);

        assertEquals("testUser", record.getUsername());
        assertEquals("madam", record.getText());
        assertTrue(record.isPalindrome());
        assertEquals(now, record.getTimestamp());
    }

    @Test
    void palindromeRequest_emptyConstructor_works() {
        PalindromeRequest request = new PalindromeRequest();
        assertNotNull(request);
        request.setUsername("testUser");
        request.setText("civic");

        assertEquals("testUser", request.getUsername());
        assertEquals("civic", request.getText());
    }
}
