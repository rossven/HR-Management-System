package com.hrms.service;

import com.hrms.dto.CandidateDTO;
import com.hrms.entity.Candidate;
import com.hrms.entity.MilitaryStatus;
import com.hrms.exception.ResourceNotFoundException;
import com.hrms.repository.CandidateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Candidate Service Tests")
class CandidateServiceTest {

    @Mock
    private CandidateRepository candidateRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private CandidateService candidateService;

    private Candidate testCandidate;
    private CandidateDTO testCandidateDTO;

    @BeforeEach
    void setUp() {
        testCandidate = Candidate.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .phone("5551234567")
                .position("Developer")
                .militaryStatus(MilitaryStatus.COMPLETED)
                .noticePeriodDays(30)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        testCandidateDTO = CandidateDTO.from(testCandidate);
    }

    @Test
    @DisplayName("Should return list of all candidates")
    void getAll() {
        when(candidateRepository.findAll()).thenReturn(Arrays.asList(testCandidate));
        List<CandidateDTO> result = candidateService.getAllCandidates();
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getFirstName()).isEqualTo("John");
        verify(candidateRepository).findAll();
    }

    @Test
    @DisplayName("Should return candidate when valid ID provided")
    void getById() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(testCandidate));
        CandidateDTO result = candidateService.getCandidate(1L);
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(candidateRepository).findById(1L);
    }

    @Test
    @DisplayName("Should throw exception when invalid ID provided")
    void getByInvalidId() {
        when(candidateRepository.findById(999L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> 
            candidateService.getCandidate(999L)
        );
        verify(candidateRepository).findById(999L);
    }

    @Test
    @DisplayName("Should update candidate with valid data")
    void update() {
        when(candidateRepository.findById(1L)).thenReturn(Optional.of(testCandidate));
        when(candidateRepository.save(any(Candidate.class))).thenReturn(testCandidate);

        testCandidateDTO.setFirstName("Jane");
        CandidateDTO result = candidateService.updateCandidate(1L, testCandidateDTO);

        assertThat(result.getFirstName()).isEqualTo("Jane");
        verify(candidateRepository).findById(1L);
        verify(candidateRepository).save(any(Candidate.class));
    }

    @Test
    @DisplayName("Should create new candidate with valid data")
    void create() {
        when(candidateRepository.save(any(Candidate.class))).thenReturn(testCandidate);
        CandidateDTO result = candidateService.createCandidate(testCandidateDTO);
        assertThat(result.getFirstName()).isEqualTo("John");
        verify(candidateRepository).save(any(Candidate.class));
    }
} 