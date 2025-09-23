package com.example.palindrome_check.controller;

import com.example.palindrome_check.model.PalindromeRequest;
import com.example.palindrome_check.model.PalindromeResponse;
import com.example.palindrome_check.service.PalindromeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/palindrome")
public class PalindromeController {

    private final PalindromeService service;

    public PalindromeController(PalindromeService service) {
        this.service = service;
    }

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestBody PalindromeRequest request) {
        String text = request.getText();

        if (text == null || text.isEmpty() || !text.matches("^[A-Za-z]+$")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Input must only contain letters, while not being null."));
        }

        boolean cached = service.isCached(text);
        boolean result = service.checkAndPersist(request.getUsername(), text);
        PalindromeResponse resp = new PalindromeResponse(
                request.getUsername(),
                text,
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
