package com.hrms.service;

import com.hrms.dto.CandidateDTO;
import com.hrms.entity.Candidate;

import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final FileStorageService fileStorageService;

    public List<CandidateDTO> getAllCandidates() {
        log.info("Getting all candidates");
        return candidateRepository.findAll().stream()
                .map(CandidateDTO::from)
                .collect(Collectors.toList());
    }

    public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
        log.info("Updating candidate with id: {}", id);
        
        Candidate candidate = getCandidateEntity(id);
        updateCandidateFields(candidate, candidateDTO);
        candidate.setUpdatedAt(LocalDateTime.now());
        
        return CandidateDTO.from(candidateRepository.save(candidate));
    }

    private void updateCandidateFields(Candidate candidate, CandidateDTO dto) {
        Optional.ofNullable(dto.getFirstName()).ifPresent(candidate::setFirstName);
        Optional.ofNullable(dto.getLastName()).ifPresent(candidate::setLastName);
        Optional.ofNullable(dto.getPosition()).ifPresent(candidate::setPosition);
        Optional.ofNullable(dto.getMilitaryStatus()).ifPresent(candidate::setMilitaryStatus);
        Optional.ofNullable(dto.getNoticePeriodMonths()).ifPresent(candidate::setNoticePeriodMonths);
        Optional.ofNullable(dto.getNoticePeriodDays()).ifPresent(candidate::setNoticePeriodDays);
        Optional.ofNullable(dto.getPhone()).ifPresent(candidate::setPhone);
        Optional.ofNullable(dto.getEmail()).ifPresent(candidate::setEmail);
        Optional.ofNullable(dto.getCvFileName()).ifPresent(candidate::setCvFileName);
        Optional.ofNullable(dto.getCvContentType()).ifPresent(candidate::setCvContentType);
        Optional.ofNullable(dto.getCvFilePath()).ifPresent(candidate::setCvFilePath);
    }

    public CandidateDTO getCandidate(Long id) {
        log.info("Getting candidate with id: {}", id);
        return CandidateDTO.from(candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found: " + id)));
    }
    
    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        log.info("Creating new candidate");
        return CandidateDTO.from(candidateRepository.save(Candidate.from(candidateDTO)));
    }
    
    public void deleteCandidate(Long id) {
        log.info("Deleting candidate with ID: {}", id);
        
        try {
            Candidate candidate = getCandidateEntity(id);
            if (candidate.getCvFileName() != null) {
                Path cvPath = fileStorageService.getFileStorageLocation().resolve(candidate.getCvFileName());
                if (Files.exists(cvPath)) {
                    Files.delete(cvPath);
                    log.info("Deleted CV file: {}", cvPath);
                }
            }

            candidateRepository.deleteById(id);
            log.info("Successfully deleted candidate with ID: {}", id);
        } catch (IOException e) {
            log.error("Error deleting CV file for candidate ID: {}", id, e);
            throw new RuntimeException("Could not delete candidate's CV file", e);
        }
    }

    public Candidate getCandidateEntity(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found: " + id));
    }
} 