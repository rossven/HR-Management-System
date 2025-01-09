package com.hrms.config;

import com.hrms.entity.Candidate;
import com.hrms.entity.MilitaryStatus;
import com.hrms.repository.CandidateRepository;
import com.hrms.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.stream.IntStream;
import java.util.HashSet;
import java.util.Set;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializationConfig {

    @Autowired
    CandidateRepository candidateRepository;

    @Autowired
    FileStorageService fileStorageService;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            log.info("Starting data initialization...");

            Path uploadDir = fileStorageService.getFileStorageLocation();
            if (Files.exists(uploadDir)) {
                log.info("Cleaning up CV directory: {}", uploadDir);
                try (Stream<Path> files = Files.list(uploadDir)) {
                    files.forEach(file -> {
                        try {
                            Files.delete(file);
                            log.info("Deleted file: {}", file);
                        } catch (IOException e) {
                            log.error("Error deleting file: {}", file, e);
                        }
                    });
                }
            }

            if (candidateRepository.count() == 0) {
                Random random = new Random();
                String[] firstNames = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Emma", "William", "Olivia"};
                String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
                
                String[] positions = {"Java Developer", "Frontend Developer", "DevOps Engineer", "Product Manager", 
                                    "UI/UX Designer", "Software Architect", "QA Engineer", "Data Scientist"};
                
                String[] noticePeriods = {"Immediate", "15", "30", "45", "60", "90"};
                
                Set<String> usedEmails = new HashSet<>();
                Set<String> usedPhones = new HashSet<>();
                
                IntStream.range(0, 10).forEach(i -> {
                    Candidate candidate = new Candidate();
                    String firstName = firstNames[random.nextInt(firstNames.length)];
                    String lastName = lastNames[random.nextInt(lastNames.length)];
                    
                    String email;
                    do {
                        email = generateEmail(firstName, lastName, random);
                    } while (usedEmails.contains(email));
                    usedEmails.add(email);
                    
                    String phone;
                    do {
                        phone = generatePhoneNumber(random);
                    } while (usedPhones.contains(phone));
                    usedPhones.add(phone);
                    
                    candidate.setFirstName(firstName);
                    candidate.setLastName(lastName);
                    candidate.setEmail(email);
                    candidate.setPhone(phone);
                    candidate.setPosition(positions[random.nextInt(positions.length)]);
                    int noticePeriodIndex = random.nextInt(noticePeriods.length);
                    candidate.setNoticePeriodDays(extractDays(noticePeriods[noticePeriodIndex]));
                    candidate.setNoticePeriodMonths(extractMonths(noticePeriods[noticePeriodIndex]));
                    candidate.setMilitaryStatus(MilitaryStatus.values()[random.nextInt(MilitaryStatus.values().length)]);
                    
                    createEmptyPDF(candidate);
                    
                    candidateRepository.save(candidate);
                });
                
                log.info("Generated 10 test candidates successfully");
            }
        };
    }

    private String generateEmail(String firstName, String lastName, Random random) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + random.nextInt(1000) + "@email.com";
    }
    
    private String generatePhoneNumber(Random random) {
        return String.format("555%07d", random.nextInt(10000000));
    }
    
    private int extractDays(String periodName) {
        if (periodName.equals("Immediate")) return 0;
        int days = Integer.parseInt(periodName);
        return days % 30;
    }
    
    private int extractMonths(String periodName) {
        if (periodName.equals("Immediate")) return 0;
        int days = Integer.parseInt(periodName);
        return days / 30;
    }

    private void createEmptyPDF(Candidate candidate) {
        try {
            String fileName = fileStorageService.createEmptyPdfAndStore(candidate);
            candidate.setCvFileName(fileName);
            candidate.setCvContentType("application/pdf");
            candidate.setCvFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());
        } catch (IOException e) {
            log.error("Error creating empty PDF", e);
        }
    }
} 