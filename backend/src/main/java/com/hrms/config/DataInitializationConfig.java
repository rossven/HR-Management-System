package com.hrms.config;

import com.hrms.entity.Candidate;
import com.hrms.entity.MilitaryStatus;
import com.hrms.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Random;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializationConfig {

    @Autowired
    CandidateRepository candidateRepository;

    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            log.info("Starting data initialization...");
            
            if (candidateRepository.count() == 0) {
                Random random = new Random();
                String[] firstNames = {"John", "Jane", "Michael", "Emily", "David", "Sarah", "James", "Emma", "William", "Olivia"};
                String[] lastNames = {"Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis", "Rodriguez", "Martinez"};
                
                String[] positions = {"Java Developer", "Frontend Developer", "DevOps Engineer", "Product Manager", 
                                    "UI/UX Designer", "Software Architect", "QA Engineer", "Data Scientist"};
                
                String[] noticePeriods = {"Immediate", "15", "30", "45", "60", "90"};
                
                IntStream.range(0, 10).forEach(i -> {
                    Candidate candidate = new Candidate();
                    candidate.setFirstName(firstNames[random.nextInt(firstNames.length)]);
                    candidate.setLastName(lastNames[random.nextInt(lastNames.length)]);
                    candidate.setEmail(generateEmail(candidate.getFirstName(), candidate.getLastName()));
                    candidate.setPhone(generatePhoneNumber(random));
                    candidate.setPosition(positions[random.nextInt(positions.length)]);
                    int noticePeriodIndex = random.nextInt(noticePeriods.length);
                    candidate.setNoticePeriodDays(extractDays(noticePeriods[noticePeriodIndex]));
                    candidate.setNoticePeriodMonths(extractMonths(noticePeriods[noticePeriodIndex]));
                    candidate.setMilitaryStatus(MilitaryStatus.values()[random.nextInt(MilitaryStatus.values().length)]);
                    
                    candidateRepository.save(candidate);
                });
                
                log.info("Generated 10 test candidates successfully");
            }
        };
    }

    private String generateEmail(String firstName, String lastName) {
        return firstName.toLowerCase() + "." + lastName.toLowerCase() + "@email.com";
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
} 