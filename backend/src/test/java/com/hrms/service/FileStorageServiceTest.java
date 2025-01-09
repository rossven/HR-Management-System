package com.hrms.service;

import com.hrms.entity.Candidate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;

@DisplayName("File Storage Service Tests")
class FileStorageServiceTest {

    private FileStorageService fileStorageService;
    
    @TempDir
    Path tempDir;

    @BeforeEach
    void setUp() {
        fileStorageService = new FileStorageService(tempDir.toString());
    }

    @Test
    @DisplayName("Should store file successfully when valid file provided")
    void storeFile() throws IOException {
        MultipartFile file = new MockMultipartFile(
                "test.pdf",
                "test.pdf",
                "application/pdf",
                "test content".getBytes()
        );

        String storedFileName = fileStorageService.storeFile(file);

        assertThat(storedFileName).isNotNull();
        assertThat(Files.exists(tempDir.resolve(storedFileName))).isTrue();
    }

    @Test
    @DisplayName("Should throw exception when empty file provided")
    void storeEmptyFile() {
        MultipartFile emptyFile = new MockMultipartFile(
                "empty.pdf",
                "empty.pdf",
                "application/pdf",
                new byte[0]
        );

        assertThrows(RuntimeException.class, () -> 
            fileStorageService.storeFile(emptyFile)
        );
    }

    @Test
    @DisplayName("Should create valid PDF file")
    void createPdf() throws IOException {
        Candidate candidate = Candidate.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        String fileName = fileStorageService.createEmptyPdfAndStore(candidate);

        assertThat(fileName).isNotNull();
        assertThat(Files.exists(tempDir.resolve(fileName))).isTrue();
        assertThat(Files.size(tempDir.resolve(fileName))).isGreaterThan(0);
    }
} 