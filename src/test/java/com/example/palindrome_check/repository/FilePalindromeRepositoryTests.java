package com.example.palindrome_check.repository;

import com.example.palindrome_check.model.PalindromeRecord;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FilePalindromeRepositoryTests {

    private FilePalindromeRepository repository;
    private ObjectMapper mapper;

    @TempDir
    Path tempDir;

    private Path tempFile;

    @BeforeEach
    void setUp() throws Exception {
        tempFile = tempDir.resolve("processed_palindromes.jsonl");
        Files.createFile(tempFile);

        repository = new FilePalindromeRepository();

        Field fileField = FilePalindromeRepository.class.getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(repository, tempFile);

        mapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    @Test
    void constructor_createsFileIfMissing() throws Exception {
        Path missingFile = tempDir.resolve("missing.jsonl");
        Files.deleteIfExists(missingFile);

        FilePalindromeRepository repo = new FilePalindromeRepository();
        Field fileField = FilePalindromeRepository.class.getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(repo, missingFile);

        PalindromeRecord record = new PalindromeRecord("u", "a", true, Instant.now());
        repo.save(record);

        assertTrue(Files.exists(missingFile));
    }


    @Test
    void save_and_findAll_singleRecord() {
        Instant now = Instant.now();
        PalindromeRecord record = new PalindromeRecord("testUser", "madam", true, now);
        repository.save(record);

        List<PalindromeRecord> results = repository.findAll();
        assertEquals(1, results.size());
        PalindromeRecord retrieved = results.get(0);
        assertEquals("testUser", retrieved.getUsername());
        assertEquals("madam", retrieved.getText());
        assertTrue(retrieved.isPalindrome());
        assertEquals(now, retrieved.getTimestamp());
    }

    @Test
    void save_and_findAll_multipleRecords() {
        Instant t1 = Instant.now();
        Instant t2 = t1.plusSeconds(60);
        PalindromeRecord r1 = new PalindromeRecord("testUser", "madam", true, t1);
        PalindromeRecord r2 = new PalindromeRecord("testUser2", "racecar", true, t2);

        repository.save(r1);
        repository.save(r2);

        List<PalindromeRecord> results = repository.findAll();
        assertEquals(2, results.size());

        assertEquals("testUser", results.get(0).getUsername());
        assertEquals("madam", results.get(0).getText());
        assertTrue(results.get(0).isPalindrome());
        assertEquals(t1, results.get(0).getTimestamp());

        assertEquals("testUser2", results.get(1).getUsername());
        assertEquals("racecar", results.get(1).getText());
        assertTrue(results.get(1).isPalindrome());
        assertEquals(t2, results.get(1).getTimestamp());
    }

    @Test
    void findAll_emptyFile_returnsEmptyList() throws Exception {
        // delete all lines to ensure empty
        Files.write(tempFile, new byte[0]);
        List<PalindromeRecord> results = repository.findAll();
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_missingTimestamp_getsPopulated() {
        PalindromeRecord record = new PalindromeRecord();
        record.setUsername("testUser");
        record.setText("level");
        record.setPalindrome(true);
        record.setTimestamp(null);

        repository.save(record);

        List<PalindromeRecord> results = repository.findAll();
        assertEquals(1, results.size());
        assertNotNull(results.get(0).getTimestamp());
    }

    @Test
    void save_throwsRuntimeException_onIOException() throws Exception {
        Path dir = tempDir.resolve("notAFile");
        Files.createDirectory(dir);

        FilePalindromeRepository repo = new FilePalindromeRepository();
        Field fileField = FilePalindromeRepository.class.getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(repo, dir);

        PalindromeRecord record = new PalindromeRecord("u", "a", true, Instant.now());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> repo.save(record));
        assertTrue(ex.getMessage().contains("Failed to save record"));
    }
    @Test
    void findAll_returnsEmptyList_ifFileMissing() throws Exception {
        Path missingFile = tempDir.resolve("missing.jsonl");
        FilePalindromeRepository repo = new FilePalindromeRepository();
        Field fileField = FilePalindromeRepository.class.getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(repo, missingFile);

        List<PalindromeRecord> results = repo.findAll();
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_skipsBlankLines() throws Exception {
        Files.writeString(tempFile, "\n");
        List<PalindromeRecord> results = repository.findAll();
        assertTrue(results.isEmpty());
    }

    @Test
    void findAll_throwsRuntimeException_onInvalidJson() throws Exception {
        Files.writeString(tempFile, "{bad json}");

        RuntimeException ex = assertThrows(RuntimeException.class, () -> repository.findAll());
        assertTrue(ex.getMessage().contains("Failed to correctly read records"));
    }
    @Test
    void constructor_throwsRuntimeException_onFileCreateFailure() throws Exception {
        Path readOnlyDir = tempDir.resolve("readonly");
        Files.createDirectory(readOnlyDir);
        readOnlyDir.toFile().setReadOnly();
        Path fileInReadOnlyDir = readOnlyDir.resolve("processed_palindromes.jsonl");

        FilePalindromeRepository repo = new FilePalindromeRepository();

        Field fileField = FilePalindromeRepository.class.getDeclaredField("file");
        fileField.setAccessible(true);
        fileField.set(repo, fileInReadOnlyDir);

        PalindromeRecord record = new PalindromeRecord("user", "madam", true, Instant.now());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> repo.save(record));
        assertTrue(ex.getMessage().contains("Failed to save record")
                || ex.getMessage().contains("Failed to initialise repository file"));
    }

}
