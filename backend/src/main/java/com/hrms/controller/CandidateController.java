package com.hrms.controller;

import com.hrms.dto.CandidateDTO;
import com.hrms.entity.Candidate;
import com.hrms.service.CandidateService;
import com.hrms.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import com.hrms.entity.MilitaryStatus;
import java.io.IOException;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
@Slf4j
public class CandidateController {

    private final CandidateService candidateService;
    private final FileStorageService fileStorageService;

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @PutMapping("/{id}")
    public ResponseEntity<CandidateDTO> updateCandidate(
            @PathVariable Long id,
            @Valid @RequestBody CandidateDTO candidate) {
        return ResponseEntity.ok(candidateService.updateCandidate(id, candidate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/cv")
    public ResponseEntity<Resource> downloadCV(@PathVariable Long id) {
        try {
            log.info("Downloading CV for candidate ID: {}", id);
            Candidate candidate = candidateService.getCandidateEntity(id);
            Resource resource = fileStorageService.loadFileAsResource(candidate.getCvFileName());

            long contentLength = resource.contentLength();
            log.info("CV file size: {} bytes", contentLength);
            
            if (contentLength == 0) {
                log.error("CV file is empty for candidate ID: {}", id);
                return ResponseEntity.badRequest().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .contentLength(contentLength)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename=\"" + candidate.getCvFileName() + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Error downloading CV for candidate ID: {}", id, e);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/with-cv")
    public ResponseEntity<CandidateDTO> updateCandidateWithCV(
            @PathVariable Long id,
            @RequestPart("data") Map<String, Object> request,
            @RequestPart("cv") MultipartFile cvFile) {
        
        try {
            log.info("Updating candidate with CV. ID: {}", id);
            
            CandidateDTO candidateDTO = createCandidateDTOFromRequest(id, request);
            addCVInfo(candidateDTO, cvFile);
            
            return ResponseEntity.ok(candidateService.updateCandidate(id, candidateDTO));
        } catch (Exception e) {
            log.error("Error updating candidate with CV", e);
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CandidateDTO> createCandidate(
            @RequestPart("data") Map<String, Object> request,
            @RequestPart("cv") MultipartFile cvFile) {
        try {
            log.info("Creating new candidate with CV");
            
            CandidateDTO candidateDTO = new CandidateDTO();
            JsonNode jsonNode = new ObjectMapper().valueToTree(request);
            
            candidateDTO.setFirstName(jsonNode.get("firstName").asText());
            candidateDTO.setLastName(jsonNode.get("lastName").asText());
            candidateDTO.setPosition(jsonNode.get("position").asText());
            candidateDTO.setMilitaryStatus(MilitaryStatus.valueOf(jsonNode.get("militaryStatus").asText()));
            candidateDTO.setNoticePeriodMonths(jsonNode.has("noticePeriodMonths") ? 
                jsonNode.get("noticePeriodMonths").asInt() : null);
            candidateDTO.setNoticePeriodDays(jsonNode.has("noticePeriodDays") ? 
                jsonNode.get("noticePeriodDays").asInt() : null);
            candidateDTO.setPhone(jsonNode.get("phone").asText());
            candidateDTO.setEmail(jsonNode.get("email").asText());

            String fileName = fileStorageService.storeFile(cvFile);
            candidateDTO.setCvFileName(fileName);
            candidateDTO.setCvContentType(cvFile.getContentType());
            candidateDTO.setCvFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());

            log.info("Successfully processed CV file: {}", fileName);
            
            CandidateDTO createdCandidate = candidateService.createCandidate(candidateDTO);
            return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error creating candidate", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private CandidateDTO createCandidateDTOFromRequest(Long id, Map<String, Object> request) {
        Candidate existingCandidate = candidateService.getCandidateEntity(id);
        JsonNode jsonNode = new ObjectMapper().valueToTree(request);

        return CandidateDTO.builder()
                .id(existingCandidate.getId())
                .firstName(getNodeTextOrDefault(jsonNode, "firstName", existingCandidate.getFirstName()))
                .lastName(getNodeTextOrDefault(jsonNode, "lastName", existingCandidate.getLastName()))
                .position(getNodeTextOrDefault(jsonNode, "position", existingCandidate.getPosition()))
                .militaryStatus(getMilitaryStatusOrDefault(jsonNode, existingCandidate.getMilitaryStatus()))
                .noticePeriodMonths(getNodeIntOrDefault(jsonNode, "noticePeriodMonths", existingCandidate.getNoticePeriodMonths()))
                .noticePeriodDays(getNodeIntOrDefault(jsonNode, "noticePeriodDays", existingCandidate.getNoticePeriodDays()))
                .phone(getNodeTextOrDefault(jsonNode, "phone", existingCandidate.getPhone()))
                .email(getNodeTextOrDefault(jsonNode, "email", existingCandidate.getEmail()))
                .cvFileName(existingCandidate.getCvFileName())
                .cvFilePath(existingCandidate.getCvFilePath())
                .cvContentType(existingCandidate.getCvContentType())
                .build();
    }

    private void addCVInfo(CandidateDTO dto, MultipartFile cvFile) throws IOException {
        String fileName = fileStorageService.storeFile(cvFile);
        dto.setCvFileName(fileName);
        dto.setCvContentType(cvFile.getContentType());
        dto.setCvFilePath(fileStorageService.getFileStorageLocation().resolve(fileName).toString());
        log.info("Successfully processed CV file: {}", fileName);
    }

    private String getNodeTextOrDefault(JsonNode node, String field, String defaultValue) {
        return node.has(field) ? node.get(field).asText() : defaultValue;
    }

    private Integer getNodeIntOrDefault(JsonNode node, String field, Integer defaultValue) {
        return node.has(field) ? node.get(field).asInt() : defaultValue;
    }

    private MilitaryStatus getMilitaryStatusOrDefault(JsonNode node, MilitaryStatus defaultValue) {
        return node.has("militaryStatus") ? 
               MilitaryStatus.valueOf(node.get("militaryStatus").asText()) : 
               defaultValue;
    }
} 