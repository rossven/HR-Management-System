package com.hrms.service;

import com.hrms.dto.CandidateDTO;
import com.hrms.entity.Candidate;
import com.hrms.entity.MilitaryStatus;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.Random;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Slf4j
public class CandidateService {

    @Autowired
    CandidateRepository candidateRepository;
    
    public List<CandidateDTO> getAllCandidates() {
        log.info("Getting all candidates");
        return candidateRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public CandidateDTO getCandidate(Long id) {
        log.info("Getting candidate with id: {}", id);
        return convertToDTO(candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidate not found: " + id)));
    }
    
    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        log.info("Creating new candidate");
        Candidate candidate = convertToEntity(candidateDTO);
        return convertToDTO(candidateRepository.save(candidate));
    }
    
    public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
        log.info("Updating candidate with id: {}", id);
        getCandidate(id); 
        candidateDTO.setId(id);
        return convertToDTO(candidateRepository.save(convertToEntity(candidateDTO)));
    }
    
    public void deleteCandidate(Long id) {
        log.info("Deleting candidate with id: {}", id);
        if (!candidateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidate not found: " + id);
        }
        candidateRepository.deleteById(id);
    }

    private CandidateDTO convertToDTO(Candidate candidate) {
        CandidateDTO dto = new CandidateDTO();
        BeanUtils.copyProperties(candidate, dto);
        return dto;
    }

    private Candidate convertToEntity(CandidateDTO dto) {
        Candidate entity = new Candidate();
        BeanUtils.copyProperties(dto, entity);
        return entity;
    }
} 