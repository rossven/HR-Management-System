package com.hrms.entity;

import com.hrms.dto.CandidateDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "candidates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String firstName;
    
    @Column(nullable = false)
    private String lastName;
    
    @Column(nullable = false)
    private String position;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MilitaryStatus militaryStatus;
    
    private Integer noticePeriodMonths;
    private Integer noticePeriodDays;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
    
    @Column(name = "cv_file_name")
    private String cvFileName;
    
    @Column(name = "cv_file_path")
    private String cvFilePath;
    
    @Column(name = "cv_content_type")
    private String cvContentType;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public static Candidate from(CandidateDTO dto) {
        return Candidate.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .position(dto.getPosition())
                .militaryStatus(dto.getMilitaryStatus())
                .noticePeriodMonths(dto.getNoticePeriodMonths())
                .noticePeriodDays(dto.getNoticePeriodDays())
                .phone(dto.getPhone())
                .email(dto.getEmail())
                .cvFileName(dto.getCvFileName())
                .cvFilePath(dto.getCvFilePath())
                .cvContentType(dto.getCvContentType())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
} 