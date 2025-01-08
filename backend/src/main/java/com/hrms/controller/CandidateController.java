package com.hrms.controller;

import com.hrms.dto.CandidateDTO;
import com.hrms.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import com.hrms.entity.MilitaryStatus;

@RestController
@RequestMapping("/api/candidates")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class CandidateController {

    private final CandidateService candidateService;

    @GetMapping
    public ResponseEntity<List<CandidateDTO>> getAllCandidates() {
        return ResponseEntity.ok(candidateService.getAllCandidates());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CandidateDTO> getCandidate(@PathVariable Long id) {
        return ResponseEntity.ok(candidateService.getCandidate(id));
    }

    @PostMapping
    public ResponseEntity<CandidateDTO> createCandidate(@Valid @RequestBody Map<String, Object> request) {
        CandidateDTO candidateDTO = new CandidateDTO();
        candidateDTO.setFirstName((String) request.get("firstName"));
        candidateDTO.setLastName((String) request.get("lastName"));
        candidateDTO.setEmail((String) request.get("email"));
        candidateDTO.setPhone((String) request.get("phone"));
        candidateDTO.setPosition((String) request.get("position"));
        candidateDTO.setMilitaryStatus(MilitaryStatus.valueOf((String) request.get("militaryStatus")));
        
        boolean hasNoticePeriod = Boolean.TRUE.equals(request.get("hasNoticePeriod"));
        if (hasNoticePeriod && request.get("noticePeriod") != null) {
            @SuppressWarnings("unchecked")
            Map<String, Integer> noticePeriod = (Map<String, Integer>) request.get("noticePeriod");
            candidateDTO.setNoticePeriodMonths(noticePeriod.get("months"));
            candidateDTO.setNoticePeriodDays(noticePeriod.get("days"));
        } else {
            candidateDTO.setNoticePeriodMonths(0);
            candidateDTO.setNoticePeriodDays(0);
        }
        
        return ResponseEntity.ok(candidateService.createCandidate(candidateDTO));
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
        return ResponseEntity.ok().build();
    }
} 