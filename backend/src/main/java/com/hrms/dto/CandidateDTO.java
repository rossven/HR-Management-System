package com.hrms.dto;

import com.hrms.entity.MilitaryStatus;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class CandidateDTO {
    private Long id;
    
    @NotBlank(message = "Ad alanı zorunludur")
    private String firstName;
    
    @NotBlank(message = "Soyad alanı zorunludur")
    private String lastName;
    
    @NotBlank(message = "Pozisyon alanı zorunludur")
    private String position;
    
    @NotNull(message = "Askerlik durumu zorunludur")
    private MilitaryStatus militaryStatus;
    
    private Integer noticePeriodMonths;
    private Integer noticePeriodDays;
    
    @NotBlank(message = "Telefon alanı zorunludur")
    @Pattern(regexp = "^[0-9]{10}$", message = "Geçerli bir telefon numarası giriniz")
    private String phone;
    
    @NotBlank(message = "Email alanı zorunludur")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;
} 