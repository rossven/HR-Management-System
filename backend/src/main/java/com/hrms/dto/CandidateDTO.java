package com.hrms.dto;

import com.hrms.entity.Candidate;
import com.hrms.entity.MilitaryStatus;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CandidateDTO {
    private Long id;
    
    @NotBlank(message = "First name is required")
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    private String lastName;
    
    @NotBlank(message = "Position is required")
    private String position;
    
    @NotNull(message = "Military status is required")
    private MilitaryStatus militaryStatus;
    
    private Integer noticePeriodMonths;
    private Integer noticePeriodDays;
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid phone number")
    private String phone;
    
    @NotBlank(message = "Email is required")
    @Email(message = "Please enter a valid email address")
    private String email;
    
    private String cvFileName;
    private String cvFilePath;
    private String cvContentType;

    public static CandidateDTO from (Candidate candidate) {
        return CandidateDTO.builder()
                .id(candidate.getId())
                .firstName(candidate.getFirstName())
                .lastName(candidate.getLastName())
                .position(candidate.getPosition())
                .militaryStatus(candidate.getMilitaryStatus())
                .noticePeriodMonths(candidate.getNoticePeriodMonths())
                .noticePeriodDays(candidate.getNoticePeriodDays())
                .phone(candidate.getPhone())
                .email(candidate.getEmail())
                .cvFileName(candidate.getCvFileName())
                .cvFilePath(candidate.getCvFilePath())
                .cvContentType(candidate.getCvContentType())
                .build();
    }
} 