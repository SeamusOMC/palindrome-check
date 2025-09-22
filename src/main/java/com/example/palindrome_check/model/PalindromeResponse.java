package com.example.palindrome_check.model;

import java.time.Instant;

public class PalindromeResponse {
    private String username;
    private String text;
    private boolean palindrome;
    private boolean cached;
    private Instant timestamp;

    public PalindromeResponse(String username, String text, boolean palindrome, boolean cached, Instant timestamp) {
        this.username = username;
        this.text = text;
        this.palindrome = palindrome;
        this.cached = cached;
        this.timestamp = timestamp;
    }

    // region getters
    public String getUsername() { return username; }
    public String getText() { return text; }
    public boolean isPalindrome() { return palindrome; }
    public boolean isCached() { return cached; }
    public Instant getTimestamp() { return timestamp; }
    //endregion
}
