package com.hrms.controller;

import com.hrms.dto.CandidateDTO;
import com.hrms.service.CandidateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

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
    public ResponseEntity<CandidateDTO> createCandidate(@Valid @RequestBody CandidateDTO candidate) {
        return ResponseEntity.ok(candidateService.createCandidate(candidate));
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