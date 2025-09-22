package com.example.palindrome_check.controller;

import com.example.palindrome_check.model.PalindromeRequest;
import com.example.palindrome_check.model.PalindromeResponse;
import com.example.palindrome_check.service.PalindromeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/palindrome")
public class PalindromeController {

    private final PalindromeService service;

    public PalindromeController(PalindromeService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<PalindromeResponse> check(@RequestBody PalindromeRequest request) {
        String text = request.getText();
        if (text == null || text.isEmpty() || !text.matches("^[A-Za-z]+$")) {
            return ResponseEntity.badRequest().body(
                    new PalindromeResponse(request.getUsername(), text, false, false, Instant.now())
            );
        }

        boolean cached = service.isCached(request.getText());
        boolean result = service.checkAndPersist(request.getUsername(), request.getText());

        PalindromeResponse resp = new PalindromeResponse(
                request.getUsername(),
                request.getText(),
                result,
                cached,
                Instant.now()
        );
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/history")
    public List<PalindromeResponse> getHistory() {
        return service.getHistory();
    }
    // a quick GET for testing purposes
    @GetMapping("/test")
    public boolean test(@RequestParam String text) {
        return PalindromeService.computePalindrome(text);
    }
}
