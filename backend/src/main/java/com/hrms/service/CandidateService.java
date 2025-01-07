package com.hrms.service;

import com.hrms.dto.CandidateDTO;
import com.hrms.entity.Candidate;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.CandidateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CandidateService {

    private final CandidateRepository candidateRepository;
    
    public List<CandidateDTO> getAllCandidates() {
        return candidateRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public CandidateDTO getCandidate(Long id) {
        return convertToDTO(candidateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Aday bulunamadı: " + id)));
    }
    
    public CandidateDTO createCandidate(CandidateDTO candidateDTO) {
        Candidate candidate = convertToEntity(candidateDTO);
        return convertToDTO(candidateRepository.save(candidate));
    }
    
    public CandidateDTO updateCandidate(Long id, CandidateDTO candidateDTO) {
        getCandidate(id); // check if exists
        candidateDTO.setId(id);
        return convertToDTO(candidateRepository.save(convertToEntity(candidateDTO)));
    }
    
    public void deleteCandidate(Long id) {
        if (!candidateRepository.existsById(id)) {
            throw new ResourceNotFoundException("Aday bulunamadı: " + id);
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