package com.example.palindrome_check.repository;

import com.example.palindrome_check.model.PalindromeRecord;

import java.util.List;

public interface PalindromeRepository {
    void save(PalindromeRecord record);
    List<PalindromeRecord> findAll();
}
