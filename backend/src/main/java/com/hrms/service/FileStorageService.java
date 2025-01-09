package com.hrms.service;

import com.hrms.entity.Candidate;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
@Slf4j
public class FileStorageService {
    private final Path fileStorageLocation;

    public FileStorageService(@Value("${file.upload-dir}") String uploadDir) {
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        try {
            Files.createDirectories(this.fileStorageLocation);
            log.info("File storage directory created: {}", this.fileStorageLocation);
        } catch (IOException ex) {
            log.error("Could not create upload directory", ex);
            throw new RuntimeException("Could not create upload directory", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        log.info("Storing file: {}", file.getOriginalFilename());
        
        if (file.isEmpty()) {
            log.error("Failed to store empty file");
            throw new RuntimeException("Failed to store empty file");
        }

        String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString() + fileExtension;

        try {
            if (originalFileName.contains("..")) {
                log.error("Invalid file path: {}", originalFileName);
                throw new RuntimeException("Invalid file path " + originalFileName);
            }

            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            if (!Files.exists(targetLocation)) {
                log.error("File was not stored successfully: {}", fileName);
                throw new RuntimeException("File was not stored successfully");
            }

            log.info("Successfully stored file: {} as {}", originalFileName, fileName);
            return fileName;
        } catch (IOException ex) {
            log.error("Could not store file {}", fileName, ex);
            throw new RuntimeException("Could not store file " + fileName, ex);
        }
    }

    public Resource loadFileAsResource(String fileName) {
        log.info("Loading file: {}", fileName);
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            
            if (resource.exists()) {
                log.info("File found: {}", fileName);
                if (resource.contentLength() == 0) {
                    log.error("File is empty: {}", fileName);
                    throw new RuntimeException("File is empty: " + fileName);
                }
                return resource;
            } else {
                log.error("File not found: {}", fileName);
                throw new RuntimeException("File not found " + fileName);
            }
        } catch (IOException ex) {
            log.error("Error loading file: {}", fileName, ex);
            throw new RuntimeException("Error loading file " + fileName, ex);
        }
    }

    public String createEmptyPdfAndStore(Candidate candidate) throws IOException {
        String fileName = UUID.randomUUID().toString() + ".pdf";
        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        
        log.info("Creating empty PDF: {}", fileName);
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 700);
            contentStream.showText(candidate.getFirstName() + " " + candidate.getLastName());
            contentStream.endText();
            contentStream.close();
            
            document.save(targetLocation.toFile());

            if (!Files.exists(targetLocation) || Files.size(targetLocation) == 0) {
                log.error("Empty PDF was not created successfully: {}", fileName);
                throw new RuntimeException("Empty PDF was not created successfully");
            }
            
            log.info("Successfully created empty PDF: {} with size: {} bytes", 
                    fileName, Files.size(targetLocation));
            return fileName;
        } catch (IOException ex) {
            log.error("Could not create empty PDF: {}", fileName, ex);
            throw new RuntimeException("Could not create empty PDF", ex);
        }
    }

    public Path getFileStorageLocation() {
        return this.fileStorageLocation;
    }
} 