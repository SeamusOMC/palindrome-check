package com.example.palindrome_check.model;

public class PalindromeRequest {
    private String username;
    private String text;


    public PalindromeRequest(){}

    public PalindromeRequest(String username, String text) {
        this.username = username;
        this.text = text;
    }

    //region getter setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    //endregion

}
