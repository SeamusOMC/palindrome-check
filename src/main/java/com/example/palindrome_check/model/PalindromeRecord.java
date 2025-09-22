package com.example.palindrome_check.model;

import java.time.Instant;

public class PalindromeRecord {
    private String username;
    private String text;
    private boolean palindrome;
    private Instant timestamp;

    public PalindromeRecord() {}

    public PalindromeRecord(String username, String text, boolean palindrome, Instant timestamp) {
        this.username = username;
        this.text = text;
        this.palindrome = palindrome;
        this.timestamp = timestamp;
    }

    // region getters setters
    public String getUsername() { return username; }
    public String getText() { return text; }
    public boolean isPalindrome() { return palindrome; }
    public Instant getTimestamp() { return timestamp; }

    public void setUsername(String username) { this.username = username; }
    public void setText(String text) { this.text = text; }
    public void setPalindrome(boolean palindrome) { this.palindrome = palindrome; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }

    //endregion
}
