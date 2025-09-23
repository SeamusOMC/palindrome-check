package com.example.palindrome_check.repository;

import com.example.palindrome_check.model.PalindromeRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Component
public class FilePalindromeRepository implements PalindromeRepository {

    private final Path file = Paths.get("data/processed_palindromes.jsonl");
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());


    public FilePalindromeRepository() {
        try {
            Files.createDirectories(file.getParent());
            if (!Files.exists(file)) {
                Files.createFile(file);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialise repository file", e);
        }
    }

    @Override
    public synchronized void save(PalindromeRecord record) {
        try {
            String line = mapper.writeValueAsString(record);
            Files.write(
                    file,
                    (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to save record", e);
        }
    }

    @Override
    public List<PalindromeRecord> findAll() {
        List<PalindromeRecord> out = new ArrayList<>();
        try {
            if (!Files.exists(file)) {
                return out;
            }

            List<String> lines = Files.readAllLines(file);
            for (String l : lines) {
                if (l.isBlank()) continue;
                PalindromeRecord r = mapper.readValue(l, PalindromeRecord.class);
                if (r.getTimestamp() == null) {
                    r.setTimestamp(Instant.now());
                }

                out.add(r);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to correctly read records", e);
        }
        return out;
    }

    public void clear() {
        try {
            if (Files.exists(file)) {
                Files.write(file, new byte[0], StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear repository", e);
        }
    }
}
